package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.outgoing.repositories

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegistrationResult
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat

interface PurchaseRepository {

    suspend fun registerPurchaseRecord(
        name: String,
        quantityPurchased: Double,
        unitFormat: UnitFormat,
        price: Double,
        storeName: String
    ): PurchaseRecordRegistrationResult
}