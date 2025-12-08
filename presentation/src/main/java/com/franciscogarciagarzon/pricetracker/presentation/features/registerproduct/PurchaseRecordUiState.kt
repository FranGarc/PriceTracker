package com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegistrationResult
import com.franciscogarciagarzon.pricetracker.presentation.UiMessage
import com.franciscogarciagarzon.pricetracker.presentation.isEmpty

data class PurchaseRecordUiState(
    val isLoading: Boolean = false,
    val result: PurchaseRecordRegistrationResult? = null,
    val successMessage: UiMessage = UiMessage.None,
    val errorMessage: UiMessage = UiMessage.None,
    val isFormEnabled: Boolean = true // State to manage UI interaction
) {
    val isIdle: Boolean
        get() =
            !isLoading && successMessage.isEmpty()
                    && errorMessage.isEmpty() && isFormEnabled

    val isSuccess: Boolean
        get() =
            !isLoading && successMessage.isEmpty().not()
                    && errorMessage.isEmpty() && isFormEnabled
}