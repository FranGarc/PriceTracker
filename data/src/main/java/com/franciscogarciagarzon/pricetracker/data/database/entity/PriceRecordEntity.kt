package com.franciscogarciagarzon.pricetracker.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat

@Entity(
    tableName = "pricerecords",
    indices = [
        Index(value = ["product_id"]),
        Index(value = ["store_id"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = arrayOf("product_id"),
            childColumns = arrayOf("product_id"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = StoreEntity::class,
            parentColumns = arrayOf("store_id"),
            childColumns = arrayOf("store_id"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ]
)
data class PriceRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val dbId: Long = 0L,
    @ColumnInfo(name = "product_id")
    val productId: Long,
    @ColumnInfo(name = "quantity_purchased")
    val quantityPurchased: Double,
    @ColumnInfo(name = "unit_format")
    val unitFormat: UnitFormat,
    @ColumnInfo(name = "price")
    val price: Double,
    @ColumnInfo(name = "store_id")
    val storeId: Long,
    @ColumnInfo(name = "purchase_date")
    val purchaseDate: Long = System.currentTimeMillis(),
)
