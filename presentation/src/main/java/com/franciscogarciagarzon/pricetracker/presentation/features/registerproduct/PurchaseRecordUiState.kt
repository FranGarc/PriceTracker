package com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct

data class PurchaseRecordUiState(
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val isFormEnabled: Boolean = true // State to manage UI interaction
) {
    val isIdle: Boolean get() = !isLoading && successMessage == null && errorMessage == null
}