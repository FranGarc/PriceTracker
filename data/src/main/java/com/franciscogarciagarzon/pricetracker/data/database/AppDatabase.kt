package com.franciscogarciagarzon.pricetracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.franciscogarciagarzon.pricetracker.data.database.dao.PriceRecordDao
import com.franciscogarciagarzon.pricetracker.data.database.dao.ProductDao
import com.franciscogarciagarzon.pricetracker.data.database.dao.StoreDao
import com.franciscogarciagarzon.pricetracker.data.database.entity.PriceRecordEntity
import com.franciscogarciagarzon.pricetracker.data.database.entity.ProductEntity
import com.franciscogarciagarzon.pricetracker.data.database.entity.StoreEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Base de Datos principal de la aplicación.
 * Centraliza la configuración de Room, incluyendo el esquema de tablas
 * y los conversores de tipos necesarios para persistir tipos complejos.
 */
@Database(
    entities = [ProductEntity::class, StoreEntity::class, PriceRecordEntity::class],
    version = 1,
    exportSchema = false // Por simplicidad, no exportamos el esquema inicialmente
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    // EXPOSICIÓN DE DAOs: Room implementará estos métodos automáticamente.
    abstract fun productDao(): ProductDao
    abstract fun storeDao(): StoreDao
    abstract fun priceRecordDao(): PriceRecordDao

    /**
     * Utilidad para el mantenimiento de la base de datos.
     * Se fuerza el uso de Dispatchers.IO para asegurar que la limpieza
     * no bloquee el hilo principal, cumpliendo con las buenas prácticas de Android.
     */
    suspend fun clearDatabase() {
        withContext(Dispatchers.IO) {
            clearAllTables()
        }
    }
}