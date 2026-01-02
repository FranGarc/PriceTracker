package com.franciscogarciagarzon.pricetracker.di

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.incoming.PurchaseRecordRegistrationPort
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegistrationUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Android/Hilt specific
abstract class DomainModule {
    /**
     * Binds the concrete implementation of the PurchaseRecordRegistrationUseCase
     * to its interface, making it injectable throughout the application.
     */
    @Binds
    @Singleton
    abstract fun bindPurchaseRecordRegistrationUseCase(
        impl: PurchaseRecordRegistrationUseCase
    ): PurchaseRecordRegistrationPort

}