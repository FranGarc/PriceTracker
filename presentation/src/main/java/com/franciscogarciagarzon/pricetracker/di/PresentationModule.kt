package com.franciscogarciagarzon.pricetracker.di

import com.franciscogarciagarzon.pricetracker.presentation.utils.StringResourceProvider
import com.franciscogarciagarzon.pricetracker.presentation.utils.StringResourceProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * PresentationModule: Módulo de Inyección de Dependencias para la capa de presentación.
 *
  */
@Module
@InstallIn(SingletonComponent::class) // Android/Hilt specific
object PresentationModule {

    /**
     * Provee una instancia única de StringResourceProvider.
     * Al marcarlo como [@Singleton], evitamos crear múltiples objetos
     * para una tarea que es puramente funcional, optimizando el uso de memoria.
     */
    @Provides
    @Singleton
    fun provideStringResourceProvider(
        impl: StringResourceProviderImpl
    ): StringResourceProvider {
        return impl
    }

}