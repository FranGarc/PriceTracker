package com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegistrationResult
import com.franciscogarciagarzon.pricetracker.presentation.UiMessage
import com.franciscogarciagarzon.pricetracker.presentation.common.StatusUiState
import com.franciscogarciagarzon.pricetracker.presentation.isEmpty

/**
 * Estado de la interfaz de usuario para el registro de compras.
 * Centraliza todas las variables que afectan a la visualización de la pantalla.
 * Al heredar de StatusUiState, estandariza el manejo de errores y carga.
 * @property isFormEnabled Determina si los inputs y botones deben responder al usuario.
 * @property result El resultado crudo de la operación de dominio (opcional).
 */
data class PurchaseRecordUiState(
    override val isLoading: Boolean = false,
    override val successMessage: UiMessage = UiMessage.None,
    override val errorMessage: UiMessage = UiMessage.None,
    val result: PurchaseRecordRegistrationResult? = null,
    val isFormEnabled: Boolean = true // State to manage UI interaction
): StatusUiState {

    /**
     * Indica si la pantalla está en su estado inicial, sin procesos activos ni mensajes.
     */
    val isIdle: Boolean
        get() =
            !isLoading && successMessage.isEmpty()
                    && errorMessage.isEmpty() && isFormEnabled

    /**
     * Indica si la última operación finalizó correctamente.
     */
    val isSuccess: Boolean
        get() =
            !isLoading && successMessage.isEmpty().not()
                    && errorMessage.isEmpty() && isFormEnabled
}