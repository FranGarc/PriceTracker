package com.franciscogarciagarzon.pricetracker.data.features.purchaselist.adapter

import com.franciscogarciagarzon.pricetracker.data.database.dao.PriceRecordDao
import com.franciscogarciagarzon.pricetracker.data.mappers.PurchaseDataMapper
import com.franciscogarciagarzon.pricetracker.domain.features.purchaselist.ports.outgoing.PurchaseListRepository
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PurchaseListRepositoryImpl @Inject constructor(
    private val priceRecordDao: PriceRecordDao,
    private val mapper: PurchaseDataMapper
) : PurchaseListRepository {

    override fun getAllPurchases(): Flow<List<PurchaseRecord>> {
        return priceRecordDao.getRecentRecordsWithDetails()
            .map { entities ->
                entities.map { mapper.toDomain(it) }
            }
    }
}