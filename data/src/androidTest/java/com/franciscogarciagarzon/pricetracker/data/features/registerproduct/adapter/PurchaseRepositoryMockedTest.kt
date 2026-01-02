package com.franciscogarciagarzon.pricetracker.data.features.registerproduct.adapter

import com.franciscogarciagarzon.pricetracker.data.database.entity.PriceRecordEntity
import com.franciscogarciagarzon.pricetracker.data.database.entity.ProductEntity
import com.franciscogarciagarzon.pricetracker.data.database.entity.StoreEntity
import com.franciscogarciagarzon.pricetracker.data.database.dao.PriceRecordDao
import com.franciscogarciagarzon.pricetracker.data.database.dao.ProductDao
import com.franciscogarciagarzon.pricetracker.data.database.dao.StoreDao
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegistrationResult
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@DisplayName("PurchaseRepository Error Path Unit Tests (Mockito-Kotlin)")
class PurchaseRepositoryMockedTest {

    // Mock dependencies: We don't want to talk to a real database here.
    private val productDao: ProductDao = mock()
    private val storeDao: StoreDao = mock()
    private val priceRecordDao: PriceRecordDao = mock()
    private lateinit var repository: PurchaseRepositoryImpl

    private val validProductName = "Milk"
    private val validStoreName = "SuperStore"
    private val validUnitFormat = UnitFormat.LITER

    @BeforeEach
    fun setup() {
        repository = PurchaseRepositoryImpl(productDao, storeDao, priceRecordDao)
        runBlocking {
            // Default Mock Behavior (Setup for Success, unless overridden by a specific test)
            // Explicitly specify String type for 'any()' to resolve compiler's type inference issue
            whenever(productDao.getProductByName(anyString())) doReturn null
            whenever(storeDao.getStoreByName(anyString())) doReturn null

            // Assume successful insertion and retrieval for Product/Store creation
            whenever(productDao.insertProduct(anyOrNull())) doReturn 1L
            whenever(productDao.getProductById(1L)) doReturn ProductEntity(1L, validProductName, validUnitFormat)

            whenever(storeDao.insertStore(anyOrNull())) doReturn 2L
            whenever(storeDao.getStoreById(2L)) doReturn StoreEntity(2L, validStoreName)

            // Assume successful price record insertion by default
            whenever(priceRecordDao.insertPriceRecord(anyOrNull<PriceRecordEntity>())) doReturn 3L
        }

    }

    @Test
    fun registerPurchaseRecord_whenPriceRecordInsertionFails_thenShouldReturnDatabaseError() {
        runTest {
            // ARRANGE: Simulate that price record insertion returns 0L (failure)
            whenever(priceRecordDao.insertPriceRecord(anyOrNull<PriceRecordEntity>())) doReturn 0L

            // ACT
            val result = repository.registerPurchaseRecord(
                name = validProductName,
                quantityPurchased = 1.0,
                unitFormat = validUnitFormat,
                price = 1.0,
                storeName = validStoreName
            )

            // ASSERT: Verifies the 'if(purchaseRecordId <=0)' path is taken.
            assert(result is PurchaseRecordRegistrationResult.DatabaseError)

            // Ensure the DAOs were called as expected up to the failure point
            verify(productDao, times(1)).insertProduct(anyOrNull<ProductEntity>())
            verify(storeDao, times(1)).insertStore(anyOrNull<StoreEntity>())
        }
    }

    @Test
    fun registerPurchaseRecord_whenProductRetrievalFailsAfterCreation_thenShouldReturnDatabaseError() {
        runTest {
            // ARRANGE: Product is new, insertion succeeds, but retrieval fails (returns null)
            whenever(productDao.insertProduct(anyOrNull<ProductEntity>())) doReturn 1L
            whenever(productDao.getProductById(1L)) doReturn null // Force this line to fail

            // ACT
            val result = repository.registerPurchaseRecord(
                name = validProductName,
                quantityPurchased = 1.0,
                unitFormat = validUnitFormat,
                price = 1.0,
                storeName = validStoreName
            )

            // ASSERT: Verifies the final 'return DatabaseError' is hit
            assert(result is PurchaseRecordRegistrationResult.DatabaseError)

            // The priceRecordDao.insertPriceRecord should NOT have been called
            // Changed to untyped any() for maximum compatibility
            verify(priceRecordDao, never()).insertPriceRecord(anyOrNull<PriceRecordEntity>())
        }
    }

    @Test
    fun registerPurchaseRecord_whenStoreRetrievalFailsAfterCreation_thenShouldReturnDatabaseErrorDueToNullStore() {
        runTest {
            // ARRANGE: Product creation succeeds, but Store insertion succeeds and retrieval fails (returns null)
            // Keep Product setup as default success
            whenever(storeDao.insertStore(anyOrNull<StoreEntity>())) doReturn 2L
            whenever(storeDao.getStoreById(2L)) doReturn null // Force this line to fail

            // ACT
            val result = repository.registerPurchaseRecord(
                name = validProductName,
                quantityPurchased = 1.0,
                unitFormat = validUnitFormat,
                price = 1.0,
                storeName = validStoreName
            )

            // ASSERT: Verifies the final 'return DatabaseError' is hit
            assert(result is PurchaseRecordRegistrationResult.DatabaseError)

            // The priceRecordDao.insertPriceRecord should NOT have been called
            verify(priceRecordDao, never()).insertPriceRecord(anyOrNull<PriceRecordEntity>())
        }
    }
}