package com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct

import com.franciscogarciagarzon.pricetracker.presentation.UiMessage

// --- 1. INTENTS: Represents all actions the user or system can take ---
sealed interface PurchaseIntent {
    data class RegisterNewPurchase(
        val productName: String,
        val quantityPurchased: String,
        val unitFormat: String,
        val price: String,
        val storeName: String
    ) : PurchaseIntent

    data object ClearStatus : PurchaseIntent
    data object SetLoading : PurchaseIntent
    data class PurchaseRegistrationSuccess(val message: UiMessage) : PurchaseIntent
    data class PurchaseRegistrationError(val message: UiMessage) : PurchaseIntent
}