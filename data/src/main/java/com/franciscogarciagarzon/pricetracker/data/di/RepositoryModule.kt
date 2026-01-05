package com.franciscogarciagarzon.pricetracker.data.di

import com.franciscogarciagarzon.pricetracker.data.database.dao.PriceRecordDao
import com.franciscogarciagarzon.pricetracker.data.database.dao.ProductDao
import com.franciscogarciagarzon.pricetracker.data.database.dao.StoreDao
import com.franciscogarciagarzon.pricetracker.data.features.recentpurchaseslist.adapter.RecentPurchaseListRepositoryImpl
import com.franciscogarciagarzon.pricetracker.data.features.registerproduct.adapter.PurchaseRepositoryImpl
import com.franciscogarciagarzon.pricetracker.data.mappers.PurchaseDataMapper
import com.franciscogarciagarzon.pricetracker.domain.features.recentpurchaseslist.ports.outgoing.RecentPurchaseListRepository
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

        val mapper = PurchaseDataMapper()

        return PurchaseRepositoryImpl(
            priceRecordDao = priceRecordDao,
            productDao = productDao,
            storeDao = storeDao,
            mapper = mapper,
        )
    }

    @Provides
    @Singleton
    fun providePurchaseListRepository(
        priceRecordDao: PriceRecordDao
    ): RecentPurchaseListRepository {
        val mapper = PurchaseDataMapper()
        return RecentPurchaseListRepositoryImpl(
            priceRecordDao = priceRecordDao,
            mapper = mapper
        )
    }
}
