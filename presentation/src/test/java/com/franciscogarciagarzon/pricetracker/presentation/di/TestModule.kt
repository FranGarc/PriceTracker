package com.franciscogarciagarzon.pricetracker.presentation.di


import com.franciscogarciagarzon.commons.utils.contracts.DispatcherProvider
import com.franciscogarciagarzon.commons.di.CommonsModule
import com.franciscogarciagarzon.commons.utils.contracts.LoggerContract
import com.franciscogarciagarzon.commons.utils.implementations.JvmLogger
import com.franciscogarciagarzon.pricetracker.presentation.util.TestDispatcher
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [CommonsModule::class] // <-- This makes it so that the dispatcher provider used in the production code is replaced by the test one in
)
@ExperimentalCoroutinesApi
abstract class TestModule {

    @Binds
    @Singleton
    abstract fun bindTestDispatcherProvider(
        testDispatcher: TestDispatcher
    ): DispatcherProvider

    @Binds
    @Singleton
    abstract fun bindLogger(impl: JvmLogger): LoggerContract
}