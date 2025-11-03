package com.franciscogarciagarzon.pricetracker.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "stores",
    // CRÍTICO: Índice de unicidad para asegurar que cada tienda se inserte una sola vez.
    indices = [Index(value = ["name"], unique = true)]
)
data class StoreEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "store_id")
    val dbId: Long = 0L,
    val name: String,
)
