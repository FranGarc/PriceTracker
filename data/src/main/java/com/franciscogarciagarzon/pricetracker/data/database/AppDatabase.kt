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

/**
 * Clase abstracta principal de Room que define la base de datos de la aplicación.
 * Define las entidades y los DAOs que Room debe implementar.
 */
@Database(
    entities = [ProductEntity::class, StoreEntity::class, PriceRecordEntity::class],
    version = 1,
    exportSchema = false // Por simplicidad, no exportamos el esquema inicialmente
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun storeDao(): StoreDao
    abstract fun priceRecordDao(): PriceRecordDao

}