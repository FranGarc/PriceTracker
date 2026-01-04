package com.franciscogarciagarzon.pricetracker.data.mappers

import com.franciscogarciagarzon.pricetracker.data.database.entity.PriceRecordEntity
import com.franciscogarciagarzon.pricetracker.data.database.entity.ProductEntity
import com.franciscogarciagarzon.pricetracker.data.database.entity.StoreEntity
import com.franciscogarciagarzon.pricetracker.data.database.model.PriceRecordWithDetails
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.Price
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.ProductName
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.QuantityPurchased
import javax.inject.Inject

class PurchaseDataMapper @Inject constructor() {

    /**
     * Transforma el POJO de lectura (JOIN de 3 tablas) al dominio.
     */
    fun toDomain(dto: PriceRecordWithDetails): PurchaseRecord {
        return PurchaseRecord(
            name = ProductName(dto.product.name),
            unitFormat = dto.product.unitFormat,
            storeName = dto.store.name,
            amount = QuantityPurchased(dto.priceRecord.quantityPurchased),
            price = Price(dto.priceRecord.price),
            purchaseDate = dto.priceRecord.purchaseDate
        )
    }

    /**
     * Sobrecarga opcional para el flujo de registro si ya tienes las entidades en memoria.
     * Esto te permite limpiar tu PurchaseRepositoryImpl.
     */
    fun toDomainFromEntities(
        priceRecord: PriceRecordEntity,
        product: ProductEntity,
        store: StoreEntity
    ): PurchaseRecord {
        return PurchaseRecord(
            name = ProductName(product.name),
            unitFormat = product.unitFormat,
            storeName = store.name,
            amount = QuantityPurchased(priceRecord.quantityPurchased),
            price = Price(priceRecord.price),
            purchaseDate = priceRecord.purchaseDate
        )
    }
}