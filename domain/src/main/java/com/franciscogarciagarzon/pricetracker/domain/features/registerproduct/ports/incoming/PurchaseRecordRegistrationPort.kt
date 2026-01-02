package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.incoming

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegisterCommand
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegistrationResult

interface PurchaseRecordRegistrationPort {
    suspend fun registerPurchaseRecord(
        command: PurchaseRecordRegisterCommand
    ): PurchaseRecordRegistrationResult
}