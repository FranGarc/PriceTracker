package com.franciscogarciagarzon.pricetracker.presentation.common

import com.franciscogarciagarzon.pricetracker.presentation.UiMessage

interface StatusUiState {
    val isLoading: Boolean
    val errorMessage: UiMessage
    val successMessage: UiMessage
}