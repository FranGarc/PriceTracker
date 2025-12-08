package com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct

import com.franciscogarciagarzon.commons.utils.Logger
import com.franciscogarciagarzon.pricetracker.presentation.UiMessage


/**
 * REDUCER: Pure function that takes the current State and an Intent, and returns a new State.
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