package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.outgoing.repositories

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegistrationResult

interface PurchaseRepository {

    suspend fun registerPurchaseRecord(
        name: String,
        quantityPurchased: Double,
        unitFormat: String,
        price: Double,
        storeName: String
    ): PurchaseRecordRegistrationResult
}