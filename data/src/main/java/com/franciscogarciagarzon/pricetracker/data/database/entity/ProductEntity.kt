package com.franciscogarciagarzon.pricetracker.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "products",
    indices = [Index(value = ["name", "unitFormat"], unique = true)]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    val dbId: Long = 0L,
    val name: String,
    val unitFormat: String,
)
