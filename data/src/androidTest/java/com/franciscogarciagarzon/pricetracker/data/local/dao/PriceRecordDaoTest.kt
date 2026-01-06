package com.franciscogarciagarzon.pricetracker.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.franciscogarciagarzon.pricetracker.data.database.AppDatabase
import com.franciscogarciagarzon.pricetracker.data.database.Converters
import com.franciscogarciagarzon.pricetracker.data.database.dao.PriceRecordDao
import com.franciscogarciagarzon.pricetracker.data.database.entity.PriceRecordEntity
import com.franciscogarciagarzon.pricetracker.data.database.entity.ProductEntity
import com.franciscogarciagarzon.pricetracker.data.database.entity.StoreEntity
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("PriceRecordDao Integration Tests")
class PriceRecordDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var priceRecordDao: PriceRecordDao

    @BeforeEach
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val gson = Gson()

        database = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).addTypeConverter(Converters(gson))
            .allowMainThreadQueries().build()
        priceRecordDao = database.priceRecordDao()
    }

    @AfterEach
    fun tearDown() {
        database.close()
    }


    // Insert y Retrieve por ID
    // Verifica que un registro se ha guardado y se puede encontrar por su productId.
    // Inserta una ProductEntity primero para obtener un  productId válido.
    // Luego inserta una PriceRecordEntity y usa un getPriceRecordByProductId para
    // verificar que se guardó correctamente.
    @Test
    fun givenInsertIdResult_WhenFindingByThatIdThePriceRecordNeedsToBeTheSame() {

        runTest {
            val testProductEntity = ProductEntity(
                name = "product test",
                unitFormat = UnitFormat.UNIT
            )
            val testProductId = database.productDao().insertProduct(testProductEntity)
            val testStoreEntity = StoreEntity(
                name = "store test"
            )
            val testStoreId = database.storeDao().insertStore(testStoreEntity)
            val priceRecordEntity = PriceRecordEntity(
                productId = testProductId,
                storeId = testStoreId,
                price = 20.2,
                quantityPurchased = 2.3,
                purchaseDate = System.currentTimeMillis(),
                unitFormat = UnitFormat.UNIT,
            )
            val insertId = priceRecordDao.insertPriceRecord(priceRecordEntity)

            val existingPriceRecord = priceRecordDao.getPriceRecordById(insertId)

            val existingPriceRecordId = existingPriceRecord?.dbId ?: -1L
            assert(existingPriceRecordId == insertId)
            val entityWithInsertId = priceRecordEntity.copy(dbId = existingPriceRecordId)
            assertEquals(entityWithInsertId, existingPriceRecord)
        }
    }


    //Múltiples registros para un Producto
    // Asegurar que el DAO devuelve una lista completa
    // cuando existen múltiples registros para el mismo producto.
    // Insertar un producto y tres  price records vinculados a ese productID.
    // Verificar que getPriceRecordByProductId devuelve una lista de tamaño 3.
    @Test
    fun givenMultipleRecordsForOneProduct_WhenFindingByProductId_RetrievesAllTheInsertedRecords() {
        runTest {
            val productEntity = ProductEntity(
                name = "some Product Name",
                unitFormat = UnitFormat.UNIT,
            )
            val testProductId = database.productDao().insertProduct(productEntity)
            val testStoreEntity = StoreEntity(
                name = "store test"
            )
            val testStoreId = database.storeDao().insertStore(testStoreEntity)
            val pairsPriceAmount = listOf(
                22.76 to 1.0,
                57.0 to 3.3,
                12.13 to 6.0,
                6.66 to 2.0,
                9.99 to 9.0,
            )
            var subtotalPrice = 0.0
            var subtotalAmount = 0.0
            pairsPriceAmount.forEach { (price, amount) ->
                subtotalPrice += price
                subtotalAmount += amount
                val priceRecordEntity = PriceRecordEntity(
                    productId = testProductId,
                    storeId = testStoreId,
                    price = price,
                    quantityPurchased = amount,
                    purchaseDate = System.currentTimeMillis(),
                    unitFormat = UnitFormat.UNIT,
                )
                priceRecordDao.insertPriceRecord(priceRecordEntity)
            }

            // assert devuelve el mismo número de registros que los insertados
            val storedRecords = priceRecordDao.getPriceRecordByProductId(testProductId)
            assertEquals(pairsPriceAmount.size, storedRecords.size)

            // assert devuelve la misma suma de precio
            val sumPrice = storedRecords.sumOf { it.price }
            // assertEquals acepta un delta de   0.001 para evitar que el test falle por pequeñas imprecisiones inevitables de punto flotante
            assertEquals(sumPrice, subtotalPrice, 0.001)
            // assert devuelve la misma suma para cantdad
            val sumAmount = storedRecords.sumOf { it.quantityPurchased }
            // assertEquals acepta un delta de   0.001 para evitar que el test falle por pequeñas imprecisiones inevitables de punto flotante
            assertEquals(sumAmount, subtotalAmount, 0.001)
        }
    }

    //Base de datos vacía
    // Asegurarse que el DAO maneja el estado vacío correctamente
    @Test
    fun whenQueryingForNonExistingRecords_returnsEmptyList() {
        // Given
        val emptyList = emptyList<PriceRecordEntity>()

        // When
        runTest {
            val getAllResult = priceRecordDao.getAll()
            val recordByProductId = priceRecordDao.getPriceRecordByProductId(1L)

            // Then
            assertEquals(recordByProductId, emptyList)
            assertEquals(getAllResult, emptyList)
        }
    }

    @Test
    fun whenDeletingPurchaseRecord_shouldRemoveFromDatabase() {
        // Given
        runTest {
            val productEntity = ProductEntity(
                name = "some Product Name",
                unitFormat = UnitFormat.UNIT,
            )
            val testProductId = database.productDao().insertProduct(productEntity)
            val testStoreEntity = StoreEntity(
                name = "store test"
            )
            val testStoreId = database.storeDao().insertStore(testStoreEntity)
            val purchase = PriceRecordEntity(
                productId = testProductId,
                quantityPurchased = 2.0,
                storeId = testStoreId,
                price = 29.99,
                unitFormat = UnitFormat.UNIT,
            )

            val insertId = priceRecordDao.insertPriceRecord(purchase)

            // When
            priceRecordDao.delete(purchase.copy(dbId = insertId))
            val allPurchases = priceRecordDao.getAll()

            // Then
            assertTrue(allPurchases.isEmpty(), "table should be empty after deleting its records")
        }
    }

    @Test
    fun insertSameProductWithDifferentPrices_shouldWork() {
        runTest {
            // Given
            val testPrices = listOf<Double>(
                10.0, 20.51, 30.47
            )

            val productEntity = ProductEntity(
                name = "some Product Name",
                unitFormat = UnitFormat.UNIT,
            )
            val testProductId = database.productDao().insertProduct(productEntity)
            val testStoreEntity = StoreEntity(
                name = "store test"
            )
            val testStoreId = database.storeDao().insertStore(testStoreEntity)
            testPrices.forEach {
                val purchase = PriceRecordEntity(
                    productId = testProductId,
                    quantityPurchased = 2.0,
                    storeId = testStoreId,
                    price = it,
                    unitFormat = UnitFormat.UNIT,
                )
                // When
                priceRecordDao.insertPriceRecord(purchase)
            }
            val result = priceRecordDao.getPriceRecordByProductId(testProductId)
            assertEquals(testPrices.size, result.size, "Should retrieve the correct number of price records for the product.")

            val retrievedPrices = result.map { it.price }
            assertEquals(testPrices, retrievedPrices.sorted(), "Retrieved prices should match the list of inserted prices.")
        }
    }


    @Test
    @DisplayName("getRecentRecordsWithDetails  should return 5 records tops, sorted by date desc")
    fun getRecentRecords_shouldReturnLimitedAndOrdered() = runTest {
        // 1. Preparar datos (1 Producto y 1 Tienda)
        val productId = database.productDao().insertProduct(ProductEntity(name = "Prod", unitFormat = UnitFormat.UNIT))
        val storeId = database.storeDao().insertStore(StoreEntity(name = "Store"))

        // 2. Insertar 7 registros con fechas incrementales (del 1 al 7)
        for (i in 1..7) {
            priceRecordDao.insertPriceRecord(
                PriceRecordEntity(
                    productId = productId,
                    storeId = storeId,
                    price = i.toDouble(),
                    quantityPurchased = 1.0,
                    purchaseDate = i * 1000L, // El id 7 será el más reciente
                    unitFormat = UnitFormat.UNIT
                )
            )
        }

        // 3. Act: Obtener el primer valor del Flow
        val recentRecords = priceRecordDao.getRecentRecordsWithDetails().first()

        // 4. Assert
        assertEquals(5, recentRecords.size, "should be limited to 5 records")
        assertEquals(7.0, recentRecords[0].priceRecord.price, "first record should be the most recent one (price 7.0)")
        assertEquals(3.0, recentRecords[4].priceRecord.price, "last record should be the fifth most recent (price 3.0)")
    }


    @Test
    @DisplayName("PriceRecordWithDetails should contain the info of product and store")
    fun getRecentRecords_shouldIncludeProductAndStoreDetails() = runTest {
        // Given
        val productName = "Aceite de Oliva"
        val storeName = "Mercadona"

        val productId = database.productDao().insertProduct(ProductEntity(name = productName, unitFormat = UnitFormat.UNIT))
        val storeId = database.storeDao().insertStore(StoreEntity(name = storeName))

        priceRecordDao.insertPriceRecord(
            PriceRecordEntity(
                productId = productId,
                storeId = storeId,
                price = 5.50,
                quantityPurchased = 1.0,
                purchaseDate = System.currentTimeMillis(),
                unitFormat = UnitFormat.UNIT
            )
        )

        // When
        val result = priceRecordDao.getRecentRecordsWithDetails().first()

        // Then
        val details = result.first()
        assertEquals(productName, details.product.name)
        assertEquals(storeName, details.store.name)
        assertEquals(5.50, details.priceRecord.price)
    }
}