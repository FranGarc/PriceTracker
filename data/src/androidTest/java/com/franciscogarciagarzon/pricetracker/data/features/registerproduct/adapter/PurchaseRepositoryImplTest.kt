package com.franciscogarciagarzon.pricetracker.data.features.registerproduct.adapter

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.franciscogarciagarzon.pricetracker.data.database.AppDatabase
import com.franciscogarciagarzon.pricetracker.data.database.entity.ProductEntity
import com.franciscogarciagarzon.pricetracker.data.database.entity.StoreEntity
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.outgoing.repositories.PurchaseRepository
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegistrationResult
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

/**
 * Pruebas de integración para la implementación de PurchaseRepositoryImpl.
 * Verifica que la lógica del repositorio interactúa correctamente con Room y los DAOs.
 */
@DisplayName("ProductRepository Integration Tests")
class PurchaseRepositoryImplTest {
    private lateinit var db: AppDatabase
    private lateinit var repository: PurchaseRepository
    private val fakeContext = ApplicationProvider.getApplicationContext<Context>()

    @BeforeEach
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            fakeContext,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        repository = PurchaseRepositoryImpl(
            productDao = db.productDao(),
            storeDao = db.storeDao(),
            priceRecordDao = db.priceRecordDao(),

            )

    }

    @AfterEach
    fun tearDown() {
        db.close()
    }

    @ParameterizedTest(name = "Given a new product and store when registering price, it should create product, store, and price record")
    @CsvSource(
        // | productName |amount | unitFormat | price | store              |
        "Leche, 1,L, 1.20, Mercamona",
        "Huevos L, 12,unidad, 2.40, Frutería Las nenas",
        "Carne picada de ternera, 400, g, 3.54, Carnicería Puri",
        "Harina de Repostería, 1, Kg, 1.10, Carreflus"
    )
    fun givenNewProductAndStore_WhenRegisteringPrice_ThenShouldCreateProductStoreAndPriceRecord(
        productName: String,
        quantityPurchased: Double,
        unitFormat: String,
        price: Double,
        storeName: String
    ) {
        runTest {

            val productNameIsNotInDatabase =
                db.productDao().getProductByName(productName)
            assertNull(productNameIsNotInDatabase)

            val productRegisterResult = repository.registerPurchaseRecord(
                name = productName,
                quantityPurchased = quantityPurchased,
                unitFormat = unitFormat,
                price = price,
                storeName = storeName
            )
            val productNameNowIsInDatabase =
                db.productDao().getProductByName(productName)
            assertNotNull(productNameNowIsInDatabase)

            val storeNameNowIsInDatabase = db.storeDao().getStoreByName(storeName)

            // PurchaseRecord Insert result
            assert(productRegisterResult is PurchaseRecordRegistrationResult.Success)
            assertNotNull(productRegisterResult.value, "PurchaseRecord must be in the database")
            assert(productRegisterResult.value?.name?.value == productName)
            assert(productRegisterResult.value?.unitFormat == unitFormat)
            assert(productRegisterResult.value?.amount?.value == quantityPurchased)
            assert(productRegisterResult.value?.price?.value == price)

            // Product inserted
            assertNotNull(productNameNowIsInDatabase, "Product must be in the database")
            assert(productRegisterResult.value?.name?.value == productNameNowIsInDatabase.name)
            assert(productRegisterResult.value?.unitFormat == productNameNowIsInDatabase.unitFormat)

            // Store inserted
            assertNotNull(storeNameNowIsInDatabase, "Store must be in the database")
            assert(productRegisterResult.value?.storeName == storeNameNowIsInDatabase.name)
        }

    }

    @ParameterizedTest(name = "Given a new product and store when registering price, it should create product, store, and price record")
    @CsvSource(
        // | productName |amount | unitFormat | price | store              |
        "Leche, 1,L, 1.20, Mercamona",
        "Huevos L, 12,unidad, 2.40, Frutería Las nenas",
        "Carne picada de ternera, 400, g, 3.54, Carnicería Puri",
        "Harina de Repostería, 1, Kg, 1.10, Carreflus"
    )
    fun givenExistingProductAndNewStore_WhenRegisteringPurchaseRecord_ThenShouldOnlyCreateStoreAndPurchaseRecord(
        existingProductName: String,
        quantityPurchased: Double,
        unitFormat: String,
        newPrice: Double,
        newStoreName: String
    ) {
        runTest {
            // 1. ARRANGE: Pre-poblar la base de datos con un Producto
            val initialProductEntity = ProductEntity(name = existingProductName, unitFormat = unitFormat)
            val existingProductId = db.productDao().insertProduct(initialProductEntity)
            val initialProductCount = db.productDao().getCount()

            // 2. ARRANGE: Asegurar que la tienda NO existe
            assertNull(db.storeDao().getStoreByName(newStoreName))

            // ACT
            repository.registerPurchaseRecord(
                name = existingProductName,
                quantityPurchased = quantityPurchased,
                unitFormat = unitFormat,
                price = newPrice,
                storeName = newStoreName
            )

            // ASSERT 1: Verificar que NO se creó un nuevo Producto
            val finalProductCount = db.productDao().getCount()
            assertEquals(initialProductCount, finalProductCount, "The product count must not increase (Product already existed).")

            // ASSERT 2: Verificar que la Tienda SÍ fue creada
            val storeInDb = db.storeDao().getStoreByName(newStoreName)
            assertNotNull(storeInDb, "The new Store must be created.")

            // ASSERT 3: Verificar que el Price Record fue creado correctamente
            val recordCount = db.priceRecordDao().getRecordCountForProduct(existingProductId)
            assertTrue(recordCount == 1, "Exactly one price record must be created and linked to the existing product.")

        }
    }

    @ParameterizedTest(name = "Given existing product and store, it should only create the price record")
    @CsvSource(
        // | existingProductName | quantity | unitFormat | price | existingStoreName |
        "Leche, 1,L, 1.20, Mercamona",
        "Leche, 1,L, 1.50, Mercamona", // A second price record
    )
    fun givenExistingProductAndExistingStore_WhenRegisteringPurchaseRecord_ThenShouldOnlyCreatePurchaseRecord(
        existingProductName: String,
        quantityPurchased: Double,
        unitFormat: String,
        newPrice: Double,
        existingStoreName: String
    ) = runTest {
        // 1. ARRANGE: Pre-populate the database with the Product and Store
        val initialProductEntity = ProductEntity(name = existingProductName, unitFormat = unitFormat)
        val existingProductId = db.productDao().insertProduct(initialProductEntity)
        val initialStoreEntity = StoreEntity(name = existingStoreName)
        db.storeDao().insertStore(initialStoreEntity)

        val initialProductCount = db.productDao().getCount()
        val initialStoreCount = db.storeDao().getCount()
        val initialRecordCount = db.priceRecordDao().getRecordCountForProduct(existingProductId)
        assertEquals(0, initialRecordCount, "Start with 0 records for the product.")

        // ACT
        val result = repository.registerPurchaseRecord(
            name = existingProductName,
            quantityPurchased = quantityPurchased,
            unitFormat = unitFormat,
            price = newPrice,
            storeName = existingStoreName
        )

        // ASSERT 1: Verify that NO new Product or Store was created
        val finalProductCount = db.productDao().getCount()
        val finalStoreCount = db.storeDao().getCount()
        assertEquals(initialProductCount, finalProductCount, "The product count must not increase.")
        assertEquals(initialStoreCount, finalStoreCount, "The store count must not increase.")

        // ASSERT 2: Verify that the Price Record WAS created correctly
        val finalRecordCount = db.priceRecordDao().getRecordCountForProduct(existingProductId)
        assertTrue(finalRecordCount == 1, "Exactly one new price record must be created.")
        assertTrue(result is PurchaseRecordRegistrationResult.Success)
    }
}