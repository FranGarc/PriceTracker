package com.franciscogarciagarzon.pricetracker.di

import com.franciscogarciagarzon.pricetracker.presentation.utils.StringResourceProvider
import com.franciscogarciagarzon.pricetracker.presentation.utils.StringResourceProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class) // Android/Hilt specific
object PresentationModule {

    @Provides
    @Singleton
    fun provideStringResourceProvider(
        impl: StringResourceProviderImpl
    ): StringResourceProvider {
        return impl
    }

}