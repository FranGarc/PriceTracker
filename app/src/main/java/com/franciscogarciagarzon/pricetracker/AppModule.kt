package com.franciscogarciagarzon.pricetracker


import com.franciscogarciagarzon.commons.di.AndroidLoggerQualifier
import com.franciscogarciagarzon.commons.utils.contracts.LoggerContract
import com.franciscogarciagarzon.pricetracker.di.AndroidLogger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    @AndroidLoggerQualifier
    abstract fun bindAndroidLogger(
        impl: AndroidLogger
    ): LoggerContract // Output: the qualified interface

    @Binds
    @Singleton
    abstract fun bindLogger(
        @AndroidLoggerQualifier logger: LoggerContract
    ): LoggerContract
}
