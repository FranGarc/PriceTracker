package com.franciscogarciagarzon.pricetracker.di

import com.franciscogarciagarzon.pricetracker.domain.features.recentpurchaseslist.ports.incoming.GetRecentPurchasesPort
import com.franciscogarciagarzon.pricetracker.domain.features.recentpurchaseslist.ports.usecases.GetRecentPurchasesUseCase
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.incoming.PurchaseRecordRegistrationPort
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegistrationUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * DomainModule: Configuración de dependencias para la lógica de negocio.
 * Vincula los Casos de Uso (implementación técnica) con sus Puertos (interfaces de entrada).
 */
@Module
@InstallIn(SingletonComponent::class) // Android/Hilt specific
abstract class DomainModule {
    /**
     * Vincula el caso de uso de registro.
     */
    @Suppress("unused")
    @Binds
    @Singleton
    abstract fun bindPurchaseRecordRegistrationUseCase(
        impl: PurchaseRecordRegistrationUseCase
    ): PurchaseRecordRegistrationPort

    /**
     * Vincula el caso de uso de consulta reactiva.
     */
    @Suppress("unused")
    @Binds
    @Singleton
    abstract fun provideGetRecentPurchasesPort(
        impl: GetRecentPurchasesUseCase
    ): GetRecentPurchasesPort
}