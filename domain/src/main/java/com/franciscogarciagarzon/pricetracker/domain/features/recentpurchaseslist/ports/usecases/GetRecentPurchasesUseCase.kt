package com.franciscogarciagarzon.pricetracker.domain.features.recentpurchaseslist.ports.usecases

import com.franciscogarciagarzon.pricetracker.domain.features.recentpurchaseslist.ports.incoming.GetRecentPurchasesPort
import com.franciscogarciagarzon.pricetracker.domain.features.recentpurchaseslist.ports.outgoing.RecentPurchaseListRepository
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentPurchasesUseCase @Inject constructor(
    private val repository: RecentPurchaseListRepository
) : GetRecentPurchasesPort {
    override fun invoke(): Flow<List<PurchaseRecord>>
        = repository.getRecentPurchases()
}