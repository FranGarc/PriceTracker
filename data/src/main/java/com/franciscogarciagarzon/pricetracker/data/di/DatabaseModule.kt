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

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

//    @Provides
//    @Singleton
//    fun provideConverters(): Converters = Converters()


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