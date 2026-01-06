package com.franciscogarciagarzon.pricetracker.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat

/**
 * Representación de persistencia para un producto.
 * Centraliza la definición de un producto para evitar la redundancia
 * de strings en la tabla de registros. Esto permite que, si un producto cambia
 * de nombre, el cambio se refleje en todos sus registros históricos.
 */
@Entity(
    tableName = "products",
    // Índice único compuesto.
    // Evita que existan dos productos con el mismo nombre y unidad
    // (ej. "Arroz - KILOGRAM"). Si el usuario intenta insertar el mismo producto
    // dos veces, la base de datos lanzará una excepción o ignorará la inserción
    // según se defina en el DAO (OnConflictStrategy).
    indices = [Index(value = ["name", "unitFormat"], unique = true)]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    val dbId: Long = 0L,
    val name: String,
    /**
     * Se almacena el UnitFormat para diferenciar productos que se venden
     * en distintos formatos (ej. "Leche" en LITER vs "Leche" en MILLILITER).
     */
    val unitFormat: UnitFormat,
)
