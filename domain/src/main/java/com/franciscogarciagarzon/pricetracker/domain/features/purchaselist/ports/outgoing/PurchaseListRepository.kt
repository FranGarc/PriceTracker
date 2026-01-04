package com.franciscogarciagarzon.pricetracker.domain.features.purchaselist.ports.outgoing

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import kotlinx.coroutines.flow.Flow

interface PurchaseListRepository {
    fun getAllPurchases(): Flow<List<PurchaseRecord>>
}