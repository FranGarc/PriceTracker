package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.outgoing.repositories

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseItemRegistrationResult

interface PurchaseRepository {

    suspend fun registerPurchaseItem(
        name: String,
        quantityPurchased: Double,
        unitFormat: String,
        price: Double,
        storeName: String
    ): PurchaseItemRegistrationResult
}