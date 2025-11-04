package com.franciscogarciagarzon.pricetracker.data.di

import android.content.Context
import androidx.room.Room
import com.franciscogarciagarzon.pricetracker.data.database.AppDatabase
import com.franciscogarciagarzon.pricetracker.data.features.registerproduct.dao.PriceRecordDao
import com.franciscogarciagarzon.pricetracker.data.features.registerproduct.dao.ProductDao
import com.franciscogarciagarzon.pricetracker.data.features.registerproduct.dao.StoreDao
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
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "price_tracker_db"
        ).build()
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