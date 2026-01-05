package com.franciscogarciagarzon.pricetracker.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Representación de persistencia para una tienda o comercio.
 * Al igual que con los productos, externalizar las tiendas a su propia
 * tabla permite una gestión centralizada. Facilita futuras funcionalidades como
 * estadísticas por comercio o geolocalización sin duplicar información.
 */
@Entity(
    tableName = "stores",
    // Índice de unicidad en el nombre.
    // Garantiza que no existan duplicados de la misma entidad física (comercio).
    // Esto es vital para que las consultas de "Precio más bajo por tienda" sean
    // precisas y no se dividan entre "Mercadona" y "Mercadona " (con espacio).
    indices = [Index(value = ["name"], unique = true)]
)
data class StoreEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "store_id")
    val dbId: Long = 0L,
    val name: String,
)
