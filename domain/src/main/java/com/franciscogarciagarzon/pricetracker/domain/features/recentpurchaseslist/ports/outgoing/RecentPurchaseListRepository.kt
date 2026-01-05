package com.franciscogarciagarzon.pricetracker.domain.features.recentpurchaseslist.ports.outgoing

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import kotlinx.coroutines.flow.Flow

interface RecentPurchaseListRepository {
    // se trata de un flujo contínuo (contrapuesto a una operacón "one-shot"), por eso no usa ResultWithValue
    fun getRecentPurchases(): Flow<List<PurchaseRecord>>
}