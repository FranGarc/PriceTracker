package com.franciscogarciagarzon.pricetracker.data.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.franciscogarciagarzon.pricetracker.data.database.entity.PriceRecordEntity
import com.franciscogarciagarzon.pricetracker.data.database.entity.ProductEntity
import com.franciscogarciagarzon.pricetracker.data.database.entity.StoreEntity

data class PriceRecordWithDetails(
    @Embedded val priceRecord: PriceRecordEntity, // @Embedded coge todas las columnas de price_records y las mete en ese campo

    @Relation( // forma declarativa de hacer un JOIN

        parentColumn = "product_id",
        entityColumn = "product_id"
    )
    val product: ProductEntity,

    @Relation(
        parentColumn = "store_id",
        entityColumn = "store_id"
    )
    val store: StoreEntity
)
