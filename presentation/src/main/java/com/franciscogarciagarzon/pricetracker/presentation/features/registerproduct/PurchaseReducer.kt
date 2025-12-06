package com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct

import com.franciscogarciagarzon.commons.utils.Logger


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
                successMessage = null,
                errorMessage = null,
                isLoading = false,
                isFormEnabled = true
            )
        }

        is PurchaseIntent.PurchaseRegistrationSuccess -> {
            currentState.copy(
                isLoading = false,
                successMessage = "Purchase record successfully registered!",
                errorMessage = null,
                isFormEnabled = true
            )
        }

        is PurchaseIntent.PurchaseRegistrationError -> {
            Logger.d("PurchaseViewModel", "reducer(intent: $intent)")

            currentState.copy(
                isLoading = false,
                successMessage = null,
                errorMessage = intent.message,
                isFormEnabled = true
            )
        }

        PurchaseIntent.SetLoading -> {
            currentState.copy(
                isLoading = true,
                isFormEnabled = false,
                successMessage = null,
                errorMessage = null
            )
        }
    }
}