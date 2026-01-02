package com.franciscogarciagarzon.pricetracker.data.features.registerproduct.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.franciscogarciagarzon.pricetracker.data.database.AppDatabase
import com.franciscogarciagarzon.pricetracker.data.database.Converters
import com.franciscogarciagarzon.pricetracker.data.database.dao.ProductDao
import com.franciscogarciagarzon.pricetracker.data.database.entity.ProductEntity
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat
import com.google.gson.Gson
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@DisplayName("ProductDao Integration Tests")
class ProductDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var productDao: ProductDao

    @BeforeEach
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val gson = Gson()

        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).addTypeConverter(Converters(gson))
            .allowMainThreadQueries().build()
        productDao = db.productDao()
    }

    @AfterEach
    fun tearDown() {
        // Cierre de la base de datos para limpiar el estado entre pruebas.
        db.close()
    }


    // Duplicate Product Name Check
    // Registering a new product name with different capitalization (e.g., "Milk" vs "milk") is handled correctly (either as separate or the same, based on domain rules).
    @ParameterizedTest
    @CsvSource(
        // | existingProductName |newInsertProductName|
        "Leche, LECHE",
        "Leche, leche",
        "Patatas Fritas, patatas fritas",
        "Patatas Fritas, Patatas fritas",
        "Patatas Fritas, PATATAS fritas",
        "Patatas Fritas, PATATAS FRITAS",
        "Patatas Fritas, PATATAS fritas",
    )
    fun givenExistingProduct_WhenRegisteringSameNameInDifferentCapitalization_ThenShouldCountAsExistingProduct(existingProductName: String, newInsertProductName: String) {

        runTest {
            val productEntity = ProductEntity(
                name = existingProductName,
                unitFormat = UnitFormat.UNIT,
            )
            val insertId = productDao.insertProduct(productEntity)

            val existingProduct = productDao.getProductByName(newInsertProductName)
            val existingProductId = existingProduct?.dbId ?: -1
            assert(existingProductId == insertId)
        }
    }

    @Test
    fun givenInsertIdResult_WhenFindingByThatIdThePriceRecordNeedsToBeTheSame() {
        runTest {
            val productEntity = ProductEntity(
                name = "some Product Name",
                unitFormat = UnitFormat.UNIT,
            )
            val insertId = productDao.insertProduct(productEntity)
            val insertedProductEntity = productDao.getProductById(insertId)

            assert(insertedProductEntity?.name == productEntity.name)
            assert(insertedProductEntity?.unitFormat == productEntity.unitFormat)
        }
    }

}