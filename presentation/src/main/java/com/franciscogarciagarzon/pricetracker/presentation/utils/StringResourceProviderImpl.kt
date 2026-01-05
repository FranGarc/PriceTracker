package com.franciscogarciagarzon.pricetracker.presentation.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Implementación basada en Android del proveedor de recursos de texto.
 * Encapsula el acceso al Context de Android para recuperar strings.
 * Al estar inyectada, permite que los componentes de la capa de presentación
 * obtengan textos localizados sin acoplarse directamente a las APIs de Android,
 * facilitando la sustitución por un Mock en pruebas unitarias.
 */
class StringResourceProviderImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
): StringResourceProvider {
    /**
     * Recupera el string del sistema de recursos de Android.
     * @param resId ID del recurso de string.
     * @param formatArgs Argumentos para rellenar placeholders en el string (ej. %s, %d).
     */
    override fun getString(resId: Int, vararg formatArgs: Any): String {
        return context.getString(resId, *formatArgs)
    }
}