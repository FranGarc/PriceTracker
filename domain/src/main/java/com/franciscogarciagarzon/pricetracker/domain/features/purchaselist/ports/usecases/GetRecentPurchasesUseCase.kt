package com.franciscogarciagarzon.pricetracker.domain.features.purchaselist.ports.usecases

import com.franciscogarciagarzon.pricetracker.domain.features.purchaselist.ports.incoming.GetRecentPurchasesPort
import com.franciscogarciagarzon.pricetracker.domain.features.purchaselist.ports.outgoing.PurchaseListRepository
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentPurchasesUseCase @Inject constructor(
    private val repository: PurchaseListRepository
) : GetRecentPurchasesPort {
    override fun invoke(): Flow<List<PurchaseRecord>>
        = repository.getAllPurchases()
}