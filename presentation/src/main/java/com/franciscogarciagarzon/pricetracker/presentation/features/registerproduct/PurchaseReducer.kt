package com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct

import com.franciscogarciagarzon.commons.utils.Logger
import com.franciscogarciagarzon.pricetracker.presentation.UiMessage


/**
 * REDUCER: Función pura que determina el siguiente estado de la UI.
 * Siguiendo el patrón MVI, el Reducer es el único lugar donde se transforma
 * el estado. Esto garantiza la "Single Source of Truth" (SSOT) y facilita la depuración,
 * ya que cada cambio de estado está vinculado a una intención específica.
 * @param currentState El estado actual e inmutable de la pantalla.
 * @param intent La intención o evento que dispara el cambio.
 * @return Una nueva instancia de PurchaseRecordUiState con los cambios aplicados.
 */
fun reducer(currentState: PurchaseRecordUiState, intent: PurchaseIntent): PurchaseRecordUiState {
    return when (intent) {
        is PurchaseIntent.RegisterNewPurchase -> {
            // Initial state update is done outside the reducer for immediate feedback
            currentState.copy(isLoading = true, isFormEnabled = false)
        }

        is PurchaseIntent.ClearStatus -> {
            // Used to reset messages (e.g., after the user dismisses the success/error banner)
            currentState.copy(
                successMessage = UiMessage.None,
                errorMessage = UiMessage.None,
                isLoading = false,
                isFormEnabled = true
            )
        }

        is PurchaseIntent.PurchaseRegistrationSuccess -> {
            currentState.copy(
                isLoading = false,
                successMessage = intent.message,
                errorMessage = UiMessage.None,
                isFormEnabled = true
            )
        }

        is PurchaseIntent.PurchaseRegistrationError -> {
            Logger.d("PurchaseViewModel", "reducer(intent: $intent)")

            currentState.copy(
                isLoading = false,
                successMessage = UiMessage.None,
                errorMessage = intent.message,
                isFormEnabled = true
            )
        }

        PurchaseIntent.SetLoading -> {
            currentState.copy(
                isLoading = true,
                isFormEnabled = false,
                successMessage = UiMessage.None,
                errorMessage = UiMessage.None
            )
        }
    }
}