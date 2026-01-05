package com.franciscogarciagarzon.pricetracker.data.di

import android.content.Context
import androidx.room.Room
import com.franciscogarciagarzon.pricetracker.data.database.AppDatabase
import com.franciscogarciagarzon.pricetracker.data.database.Converters
import com.franciscogarciagarzon.pricetracker.data.database.dao.PriceRecordDao
import com.franciscogarciagarzon.pricetracker.data.database.dao.ProductDao
import com.franciscogarciagarzon.pricetracker.data.database.dao.StoreDao
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt para la provisión de dependencias de la base de datos.
 * Centraliza la creación de objetos costosos (Database) y asegura
 * que existan como instancias únicas (Singletons) durante toda la vida de la app.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    /**
     * Configuración y construcción de la base de datos Room.
     * Se utiliza .addTypeConverter(Converters(gson)) manualmente.
     * Dado que nuestra clase Converters requiere Gson en su constructor,
     * no podemos dejar que Room la instancie por defecto. Al pasarla aquí,
     * garantizamos que el motor de serialización esté correctamente configurado.
     */
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context, gson: Gson): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "price_tracker_db"
        )
        .addTypeConverter(Converters(gson))
        .build()
    }

    // PROVISIÓN DE DAOs: Permite que los repositorios soliciten directamente
    // el DAO que necesiten sin depender de toda la base de datos.
    @Provides
    fun providePriceRecordDao(db: AppDatabase): PriceRecordDao {
        return db.priceRecordDao()
    }

    @Provides
    fun provideProductDao(db: AppDatabase): ProductDao {
        return db.productDao()
    }

    @Provides
    fun provideStoreDao(db: AppDatabase): StoreDao {
        return db.storeDao()
    }
}