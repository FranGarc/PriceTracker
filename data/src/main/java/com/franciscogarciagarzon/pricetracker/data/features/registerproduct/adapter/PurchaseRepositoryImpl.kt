package com.franciscogarciagarzon.pricetracker.data.features.registerproduct.adapter

import android.util.Log
import com.franciscogarciagarzon.pricetracker.data.database.dao.PriceRecordDao
import com.franciscogarciagarzon.pricetracker.data.database.dao.ProductDao
import com.franciscogarciagarzon.pricetracker.data.database.dao.StoreDao
import com.franciscogarciagarzon.pricetracker.data.database.entity.PriceRecordEntity
import com.franciscogarciagarzon.pricetracker.data.database.entity.ProductEntity
import com.franciscogarciagarzon.pricetracker.data.database.entity.StoreEntity
import com.franciscogarciagarzon.pricetracker.data.mappers.PurchaseDataMapper
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.outgoing.repositories.PurchaseRepository
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegistrationResult
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat
import javax.inject.Inject

class PurchaseRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val storeDao: StoreDao,
    private val priceRecordDao: PriceRecordDao,
    private val mapper: PurchaseDataMapper
) : PurchaseRepository {
    override suspend fun registerPurchaseRecord(
        name: String,
        quantityPurchased: Double,
        unitFormat: UnitFormat,
        price: Double,
        storeName: String
    ): PurchaseRecordRegistrationResult {

        return try {
            // 1. Manejo de Producto
            val productInDb = productDao.getProductByName(name) ?: run {
                val newEntity = ProductEntity(name = name, unitFormat = unitFormat)
                val id = productDao.insertProduct(newEntity)
                if (id <= 0) return PurchaseRecordRegistrationResult.DatabaseError
                newEntity.copy(dbId = id) // Evitamos el getProductById
            }

            // 2. Manejo de Tienda
            val storeInDb = storeDao.getStoreByName(storeName) ?: run {
                val newEntity = StoreEntity(name = storeName)
                val id = storeDao.insertStore(newEntity)
                if (id <= 0) return PurchaseRecordRegistrationResult.DatabaseError
                newEntity.copy(dbId = id) // Evitamos el getStoreById
            }

            // 3. Creación del registro
            val priceRecordEntity = PriceRecordEntity(
                productId = productInDb.dbId,
                storeId = storeInDb.dbId,
                quantityPurchased = quantityPurchased,
                unitFormat = unitFormat,
                price = price
            )

            val purchaseRecordId = priceRecordDao.insertPriceRecord(priceRecordEntity)

            if (purchaseRecordId > 0) {
                // Usamos el mapper con las entidades que ya tenemos "frescas" en memoria
                val domainRecord = mapper.toDomainFromEntities(
                    priceRecordEntity.copy(dbId = purchaseRecordId),
                    productInDb,
                    storeInDb
                )
                PurchaseRecordRegistrationResult.Success(domainRecord)
            } else {
                PurchaseRecordRegistrationResult.DatabaseError
            }
        } catch (e: Exception) {
            Log.e("PurchaseRepository", "Error registering purchase", e)
            PurchaseRecordRegistrationResult.DatabaseError
        }
    }
}