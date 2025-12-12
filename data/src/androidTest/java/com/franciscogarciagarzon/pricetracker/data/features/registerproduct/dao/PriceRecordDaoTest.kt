package com.franciscogarciagarzon.pricetracker.data.features.registerproduct.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.franciscogarciagarzon.pricetracker.data.database.AppDatabase
import com.franciscogarciagarzon.pricetracker.data.database.Converters
import com.franciscogarciagarzon.pricetracker.data.database.entity.PriceRecordEntity
import com.franciscogarciagarzon.pricetracker.data.database.entity.ProductEntity
import com.franciscogarciagarzon.pricetracker.data.database.entity.StoreEntity
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat
import com.google.gson.Gson
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


    // Insert and Retrieve by ID
    // Verify a record is saved and can be found by its associated productId.	Insert a ProductEntity first to get a valid productId. Then insert a PriceRecordEntity and use getPriceRecordByProductId to verify it was saved correctly.
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


    //Multiple Records for One Product
    // Ensure the DAO returns a complete List when multiple records exist for the same product.	Insert one product and three price records linked to that product ID. Assert that getPriceRecordByProductId returns a list of size 3.
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
            val pairsPriceAmount = listOf<Pair<Double, Double>>(
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

            // assert returns same number of records as inserted
            val storedRecords = priceRecordDao.getPriceRecordByProductId(testProductId)
            assertEquals(pairsPriceAmount.size, storedRecords.size)

            // assert returns same sum for price
            val sumPrice = storedRecords.sumOf { it.price }
            // assertEquals accepts a 0.001 delta to avoid test failures due to tiny, unavoidable floating-point inaccuracies
            assertEquals(sumPrice, subtotalPrice, 0.001)
            // assert returns same sum for quantity
            val sumAmount = storedRecords.sumOf { it.quantityPurchased }
            // assertEquals accepts a 0.001 delta to avoid test failures due to tiny, unavoidable floating-point inaccuracies
            assertEquals(sumAmount, subtotalAmount, 0.001)
        }
    }

    //Empty Database
    // Ensure the DAO handles an empty state gracefully.
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


}