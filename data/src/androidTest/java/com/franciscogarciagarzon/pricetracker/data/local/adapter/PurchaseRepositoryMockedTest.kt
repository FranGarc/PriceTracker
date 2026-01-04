package com.franciscogarciagarzon.pricetracker.data.local.adapter

import com.franciscogarciagarzon.pricetracker.data.database.dao.PriceRecordDao
import com.franciscogarciagarzon.pricetracker.data.database.dao.ProductDao
import com.franciscogarciagarzon.pricetracker.data.database.dao.StoreDao
import com.franciscogarciagarzon.pricetracker.data.database.entity.PriceRecordEntity
import com.franciscogarciagarzon.pricetracker.data.database.entity.ProductEntity
import com.franciscogarciagarzon.pricetracker.data.database.entity.StoreEntity
import com.franciscogarciagarzon.pricetracker.data.features.registerproduct.adapter.PurchaseRepositoryImpl
import com.franciscogarciagarzon.pricetracker.data.mappers.PurchaseDataMapper
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegistrationResult
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@DisplayName("PurchaseRepository Error Path Unit Tests (Mockito-Kotlin)")
class PurchaseRepositoryMockedTest {

    // dependencias mockeadas: no queremos hablar con la base de datos aquí
    private val productDao: ProductDao = mock()
    private val storeDao: StoreDao = mock()
    private val priceRecordDao: PriceRecordDao = mock()
    private lateinit var repository: PurchaseRepositoryImpl

    private val validProductName = "Milk"
    private val validStoreName = "SuperStore"
    private val validUnitFormat = UnitFormat.LITER

    @BeforeEach
    fun setup() {
        val mapper = PurchaseDataMapper()

        repository = PurchaseRepositoryImpl(productDao, storeDao, priceRecordDao, mapper)
        runBlocking {
            // Comportamiento Mock por defecto (Setup para Success, salvo que un test específico lo cambie)
            whenever(productDao.getProductByName(anyString())) doReturn null
            whenever(storeDao.getStoreByName(anyString())) doReturn null

            // Asumir inserción y recuperación de exitosas para la creación de Product/Store
            whenever(productDao.insertProduct(anyOrNull())) doReturn 1L
            whenever(productDao.getProductById(1L)) doReturn ProductEntity(1L, validProductName, validUnitFormat)

            whenever(storeDao.insertStore(anyOrNull())) doReturn 2L
            whenever(storeDao.getStoreById(2L)) doReturn StoreEntity(2L, validStoreName)

            // Asumir una inserción exitosa de  price record por defecto
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
    fun registerPurchaseRecord_whenProductInsertionFails_thenShouldReturnDatabaseError() {
        runTest {
            // ARRANGE: Ahora forzamos el fallo en el INSERT, que es lo que el código SI usa
            whenever(productDao.insertProduct(anyOrNull())).doReturn(0L)

            // ACT
            val result = repository.registerPurchaseRecord(
                name = validProductName,
                quantityPurchased = 1.0,
                unitFormat = validUnitFormat,
                price = 1.0,
                storeName = validStoreName
            )

            // ASSERT
            assert(result is PurchaseRecordRegistrationResult.DatabaseError)
        }
    }

    @Test
    fun registerPurchaseRecord_whenStoreInsertionFails_thenShouldReturnDatabaseError() {
        runTest {
            // ARRANGE: Forzamos fallo en el insert de la tienda
            whenever(storeDao.insertStore(anyOrNull())).doReturn(0L)

            // ACT
            val result = repository.registerPurchaseRecord(
                name = validProductName,
                quantityPurchased = 1.0,
                unitFormat = validUnitFormat,
                price = 1.0,
                storeName = validStoreName
            )

            // ASSERT
            assert(result is PurchaseRecordRegistrationResult.DatabaseError)
        }
    }
}