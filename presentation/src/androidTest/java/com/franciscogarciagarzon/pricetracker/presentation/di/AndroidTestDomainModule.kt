package com.franciscogarciagarzon.pricetracker.presentation.di

import com.franciscogarciagarzon.pricetracker.di.DomainModule
import com.franciscogarciagarzon.pricetracker.domain.features.purchaselist.ports.incoming.GetRecentPurchasesPort
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.incoming.PurchaseRecordRegistrationPort
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegistrationResult
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DomainModule::class]
)
object AndroidTestDomainModule {

    @Provides
    @Singleton
    fun providePurchaseRecordRegistrationPort(): PurchaseRecordRegistrationPort {
        // Mantenemos tu configuración original exacta
        return mock<PurchaseRecordRegistrationPort> {
            onBlocking { registerPurchaseRecord(any()) } doReturn PurchaseRecordRegistrationResult.Success(null)
        }
    }

    @Provides
    @Singleton
    fun provideGetRecentPurchasesPort(): GetRecentPurchasesPort {
        // Añadimos este mock para que Hilt no falle.
        // Devuelve un flujo de lista vacía, neutral para tus tests de registro.
        return mock<GetRecentPurchasesPort> {
            on { invoke() } doReturn flowOf(emptyList())
        }
    }
}