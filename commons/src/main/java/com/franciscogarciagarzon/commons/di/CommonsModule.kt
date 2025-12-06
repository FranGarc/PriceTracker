package com.franciscogarciagarzon.commons.di


import com.franciscogarciagarzon.commons.utils.implementations.DefaultDispatcherProvider
import com.franciscogarciagarzon.commons.utils.contracts.DispatcherProvider
import com.franciscogarciagarzon.commons.utils.contracts.LoggerContract
import com.franciscogarciagarzon.commons.utils.implementations.JvmLogger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class CommonsModule {

    @Binds
    @Singleton
    abstract fun bindDispatcherProvider(
        defaultDispatcherProvider: DefaultDispatcherProvider
    ): DispatcherProvider

    @Binds
    @Singleton
    @JvmLoggerQualifier
    abstract fun bindLogger(
        jvmLogger: JvmLogger
    ): LoggerContract
}