package com.franciscogarciagarzon.pricetracker.data.features.registerproduct.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.franciscogarciagarzon.pricetracker.data.database.AppDatabase
import com.franciscogarciagarzon.pricetracker.data.database.Converters
import com.franciscogarciagarzon.pricetracker.data.database.entity.StoreEntity
import com.google.gson.Gson
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.api.Assertions.assertEquals

@DisplayName("StoreDao Integration Tests")
class StoreDaoTest {


    private lateinit var db: AppDatabase
    private lateinit var storeDao: StoreDao

    @BeforeEach
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val gson = Gson()

        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).addTypeConverter(Converters(gson))
            .allowMainThreadQueries().build()
        storeDao = db.storeDao()
    }

    @AfterEach
    fun tearDown() {
        // Cierre de la base de datos para limpiar el estado entre pruebas.
        db.close()
    }


    // Duplicate Store Name Check
    // Registering a new product name with different capitalization (e.g., "Milk" vs "milk") is handled correctly (either as separate or the same, based on domain rules).
    @ParameterizedTest
    @CsvSource(
        // | existingStoreName |newInsertStoreName|
        "Mercamona, MERCAMONA",
        "Mercamona, mercamona",
        "El Corte Inglés, el corte inglés",
        "El Corte Inglés, El corte Inglés",
        "El Corte Inglés, el Corte Inglés",
        "El Corte Inglés, El Corte Inglés",
        "El Corte Inglés, el CORTE inglés",
    )
    fun givenExistingStore_WhenRegisteringSameNameInDifferentCapitalization_ThenShouldCountAsExistingStore(existingStoreName: String, newInsertStoreName: String) {

        runTest {
            val storeEntity = StoreEntity(
                name = existingStoreName,
            )
            val insertId = storeDao.insertStore(storeEntity)

            val existingStore = storeDao.getStoreByName(newInsertStoreName)
            val existingStoreId = existingStore?.dbId ?: -1

            assertEquals(existingStoreId, insertId, "Retrieved dbId by name needs to be the same to the insert result")
        }
    }

    @Test
    fun givenInsertIdResult_WhenFindingByThatIdTheStoreNeedsToBeTheSame() {
        runTest {
            val storeEntity = StoreEntity(
                name = "test name",
            )
            val insertId = storeDao.insertStore(storeEntity)
            val existingStore = storeDao.getStoreById(insertId)

            assertEquals(existingStore?.name, storeEntity.name, "Retrieved store name needs to be the same as the inserted store name")
        }
    }

}