package com.franciscogarciagarzon.pricetracker.presentation.features.recentpurchaseslist

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord

sealed interface PurchaseListUiState {
    data object Loading : PurchaseListUiState
    data class Success(val purchases: List<PurchaseRecord>) : PurchaseListUiState
    data object Empty : PurchaseListUiState
    data class Error(val message: String) : PurchaseListUiState
}