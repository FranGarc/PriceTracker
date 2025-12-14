package com.franciscogarciagarzon.pricetracker.presentation.di

import com.franciscogarciagarzon.pricetracker.di.DomainModule
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.incoming.PurchaseRecordRegistrationPort
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegistrationResult
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DomainModule::class] // This tells Hilt to use this module INSTEAD of DomainModule
)
object AndroidTestDomainModule {
    @Provides
    @Singleton
    fun providePurchaseRecordRegistrationPort(): PurchaseRecordRegistrationPort {
        // Here we are providing a mock instance of the interface.
        // Hilt will inject this mock whenever the interface is requested in a test.
        return mock<PurchaseRecordRegistrationPort> {
            // By default, for any command, return Success.
            onBlocking { registerPurchaseRecord(any()) } doReturn PurchaseRecordRegistrationResult.Success(null)
        }
    }
}