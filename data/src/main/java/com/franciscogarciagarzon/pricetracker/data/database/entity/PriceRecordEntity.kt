package com.franciscogarciagarzon.pricetracker.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat

/**
 * Representación de persistencia para un registro de precio/compra.
 * Esta clase actúa como la "Tabla" en Room. Se separa de la entidad de dominio
 * para permitir que el esquema de la base de datos evolucione (normalización, índices)
 * sin afectar la lógica de negocio.
 */
@Entity(
    tableName = "pricerecords",
    indices = [
        Index(value = ["product_id"]),
        Index(value = ["store_id"])
    ],
    // Implementación de Integridad Referencial.
    // CASCADE asegura que si se borra un producto o una tienda, sus registros de
    // precios asociados se eliminen automáticamente, manteniendo la BD limpia.
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
    /**
     * Clave primaria técnica autoincremental.
     * Se utiliza un Long autoincremental gestionado por SQLite para
     * simplificar la inserción y garantizar la unicidad a nivel de fila.
     */
    @PrimaryKey(autoGenerate = true)
    val dbId: Long = 0L,
    @ColumnInfo(name = "product_id")
    val productId: Long,
    @ColumnInfo(name = "quantity_purchased")
    val quantityPurchased: Double,
    /**
     * El formato de unidad se guarda como enum.
     * NOTE: Room requerirá un TypeConverter para mapear este enum a String o Int.
     */
    @ColumnInfo(name = "unit_format")
    val unitFormat: UnitFormat,
    @ColumnInfo(name = "price")
    val price: Double,
    @ColumnInfo(name = "store_id")
    val storeId: Long,
    @ColumnInfo(name = "purchase_date")
    val purchaseDate: Long = System.currentTimeMillis(),
)
