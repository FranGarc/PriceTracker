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

/**
 * Módulo de Hilt para la provisión de repositorios.
 * Este módulo actúa como el "enlazador" entre las interfaces de dominio
 * y las implementaciones de infraestructura. Permite que el resto de la app dependa
 * de abstracciones, facilitando el intercambio de la fuente de datos (ej. pasar de Room a una API).
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePurchaseRepository(
        priceRecordDao: PriceRecordDao,
        productDao: ProductDao,
        storeDao: StoreDao,
        mapper: PurchaseDataMapper
    ): PurchaseRepository {
        return PurchaseRepositoryImpl(
            priceRecordDao = priceRecordDao,
            productDao = productDao,
            storeDao = storeDao,
            mapper = mapper
        )
    }

    @Provides
    @Singleton
    fun providePurchaseListRepository(
        priceRecordDao: PriceRecordDao,
        mapper: PurchaseDataMapper
    ): RecentPurchaseListRepository {
        return RecentPurchaseListRepositoryImpl(
            priceRecordDao = priceRecordDao,
            mapper = mapper
        )
    }
}
