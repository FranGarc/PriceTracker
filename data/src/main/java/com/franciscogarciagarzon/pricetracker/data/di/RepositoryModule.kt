package com.franciscogarciagarzon.pricetracker.data.di

import com.franciscogarciagarzon.pricetracker.data.features.registerproduct.adapter.PurchaseRepositoryImpl
import com.franciscogarciagarzon.pricetracker.data.database.dao.PriceRecordDao
import com.franciscogarciagarzon.pricetracker.data.database.dao.ProductDao
import com.franciscogarciagarzon.pricetracker.data.database.dao.StoreDao
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.outgoing.repositories.PurchaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePurchaseRepository(
        priceRecordDao: PriceRecordDao,
        productDao: ProductDao,
        storeDao: StoreDao
    ): PurchaseRepository {
        // You would need to create PurchaseRepositoryImpl and ensure its constructor
        // is annotated with @Inject or passed the required DAOs.
        return PurchaseRepositoryImpl(
            priceRecordDao = priceRecordDao,
            productDao = productDao,
            storeDao = storeDao
        )
    }
}
