package com.franciscogarciagarzon.pricetracker.domain.features.purchaselist.ports.incoming

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import kotlinx.coroutines.flow.Flow

interface GetRecentPurchasesPort {
    operator fun invoke(): Flow<List<PurchaseRecord>>
}