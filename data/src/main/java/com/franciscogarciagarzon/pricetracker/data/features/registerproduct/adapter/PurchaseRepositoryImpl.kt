package com.franciscogarciagarzon.pricetracker.data.features.registerproduct.adapter

import com.franciscogarciagarzon.pricetracker.data.database.entity.PriceRecordEntity
import com.franciscogarciagarzon.pricetracker.data.database.entity.ProductEntity
import com.franciscogarciagarzon.pricetracker.data.database.entity.StoreEntity
import com.franciscogarciagarzon.pricetracker.data.features.registerproduct.dao.PriceRecordDao
import com.franciscogarciagarzon.pricetracker.data.features.registerproduct.dao.ProductDao
import com.franciscogarciagarzon.pricetracker.data.features.registerproduct.dao.StoreDao
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.outgoing.repositories.PurchaseRepository
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegistrationResult
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.Price
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.ProductName
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.QuantityPurchased

class PurchaseRepositoryImpl(
    private val productDao: ProductDao,
    private val storeDao: StoreDao,
    private val priceRecordDao: PriceRecordDao,
) : PurchaseRepository {
    override suspend fun registerPurchaseRecord(
        name: String,
        quantityPurchased: Double,
        unitFormat: String,
        price: Double,
        storeName: String
    ): PurchaseRecordRegistrationResult {

        // check existance of product name
        var productInDb = productDao.getProductByName(name)
        // create if non existing
        if (productInDb == null) {
            val productEntity = ProductEntity(
                name = name,
                unitFormat = unitFormat
            )
            val newId = productDao.insertProduct(productEntity)
            productInDb = productDao.getProductById(newId)
        }
        // check existance of store name
        var storeInDb = storeDao.getStoreByName(storeName)
        // create if non existing
        if (storeInDb == null) {
            val storeEntity = StoreEntity(name = storeName)
            val newId = storeDao.insertStore(storeEntity)
            storeInDb = storeDao.getStoreById(newId)
        }
        // create price record
        if (productInDb != null && storeInDb != null) {
            val priceRecordEntity = PriceRecordEntity(
                productId = productInDb.dbId,
                storeId = storeInDb.dbId,
                quantityPurchased = quantityPurchased,
                price = price,
                purchaseDate = System.currentTimeMillis(),
            )

            val purchaseRecordId = priceRecordDao.insertPriceRecord(priceRecordEntity)

            if (purchaseRecordId <= 0) {
                return PurchaseRecordRegistrationResult.DatabaseError
            }

            // return created price record
            val purchaseRecord = PurchaseRecord(
                name = ProductName(productInDb.name),
                unitFormat = productInDb.unitFormat,
                storeName = storeInDb.name,
                amount = QuantityPurchased(priceRecordEntity.quantityPurchased),
                price = Price(priceRecordEntity.price),
                purchaseDate = priceRecordEntity.purchaseDate
            )
            return PurchaseRecordRegistrationResult.Success(purchaseRecord)
        }
        return PurchaseRecordRegistrationResult.DatabaseError
    }
}