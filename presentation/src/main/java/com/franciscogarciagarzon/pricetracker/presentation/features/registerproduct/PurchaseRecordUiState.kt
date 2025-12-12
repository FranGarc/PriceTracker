package com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegistrationResult
import com.franciscogarciagarzon.pricetracker.presentation.UiMessage
import com.franciscogarciagarzon.pricetracker.presentation.common.StatusUiState
import com.franciscogarciagarzon.pricetracker.presentation.isEmpty

data class PurchaseRecordUiState(
    override val isLoading: Boolean = false,
    override val successMessage: UiMessage = UiMessage.None,
    override val errorMessage: UiMessage = UiMessage.None,
    val result: PurchaseRecordRegistrationResult? = null,
    val isFormEnabled: Boolean = true // State to manage UI interaction
): StatusUiState {
    val isIdle: Boolean
        get() =
            !isLoading && successMessage.isEmpty()
                    && errorMessage.isEmpty() && isFormEnabled

    val isSuccess: Boolean
        get() =
            !isLoading && successMessage.isEmpty().not()
                    && errorMessage.isEmpty() && isFormEnabled
}