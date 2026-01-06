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

/**
 * Mapper encargado de la transformación de modelos de persistencia a entidades de negocio.
 * Actúa como una "frontera" de seguridad. Al convertir los datos aquí,
 * nos aseguramos de que cualquier dato que entre en el dominio sea validado
 * automáticamente por los constructores de los Value Objects (ProductName, Price, etc.).
 */
class PurchaseDataMapper @Inject constructor() {

    /**
     * Transforma el POJO relacional (resultado de un JOIN de 3 tablas) en una entidad PurchaseRecord.
     * Se extraen los nombres de las entidades relacionadas (Product y Store)
     * para "aplanar" la estructura tal como la espera el dominio.
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
     * Recompone una entidad de dominio a partir de sus componentes de persistencia individuales.
     * Útil tras operaciones de escritura donde ya disponemos de las
     * entidades recién creadas o recuperadas, evitando una nueva consulta a la BD.
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