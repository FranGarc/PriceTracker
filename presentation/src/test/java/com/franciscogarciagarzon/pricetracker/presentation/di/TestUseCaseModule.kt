package com.franciscogarciagarzon.pricetracker.presentation.di

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.incoming.PurchaseRecordRegistrationPort
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.mockito.Mockito.mock
import javax.inject.Singleton

/**
 * Provides a singleton mock of the usecase ports.
 * These mock will be injected into any class that requires this dependency
 * during a Hilt test.
 * This prevents Hilt from creating the real UseCase and its dependencies (Repository, DB).
 */

@Module
@InstallIn(SingletonComponent::class)
object TestUseCaseModule {


    @Provides
    @Singleton
    fun provideMockPurchaseRecordRegistrationPort(): PurchaseRecordRegistrationPort {
        return mock(PurchaseRecordRegistrationPort::class.java)
    }
}
