package com.franciscogarciagarzon.pricetracker.presentation.utils

import androidx.annotation.StringRes

/**
 * Abstracción para el acceso a recursos de texto de Android.
 * En Clean Architecture, queremos evitar que las capas de lógica tengan
 * dependencias directas de clases de Android (como Context). Esta interfaz permite
 * que el ViewModel solicite strings formateados de manera testable.
 */
interface StringResourceProvider {
    /**
     * Recupera un string de los recursos del sistema.
     * @param resId El identificador del recurso (R.string.xxx).
     * @param formatArgs Argumentos opcionales para el formateo del string.
     */
    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String
}