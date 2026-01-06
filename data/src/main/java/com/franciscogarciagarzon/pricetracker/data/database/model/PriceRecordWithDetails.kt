package com.franciscogarciagarzon.pricetracker.data.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.franciscogarciagarzon.pricetracker.data.database.entity.PriceRecordEntity
import com.franciscogarciagarzon.pricetracker.data.database.entity.ProductEntity
import com.franciscogarciagarzon.pricetracker.data.database.entity.StoreEntity

/**
 * Modelo de datos intermedio para consultas relacionales.
 * Room no permite que las entidades tengan objetos anidados directamente.
 * Esta clase permite "recomponer" la información dispersa en tres tablas
 * (registros, productos y tiendas) en una sola estructura de datos.
 */
data class PriceRecordWithDetails(
    /**
     * @Embedded: Extrae todas las columnas de la tabla 'pricerecords' y las
     * mapea directamente a los campos de PriceRecordEntity.
     */
    @Embedded val priceRecord: PriceRecordEntity,

    /**
     * @Relation: Define de forma declarativa un JOIN automático.
     * Room buscará en la tabla 'products' el registro cuyo 'product_id'
     * coincida con el de nuestro registro de precio.
     */
    @Relation(
        parentColumn = "product_id",
        entityColumn = "product_id"
    )
    val product: ProductEntity,

    /**
     * @Relation: Similar a la anterior, vincula la tienda correspondiente.
     * Facilita la obtención del nombre de la tienda sin consultas manuales extra.
     */
    @Relation(
        parentColumn = "store_id",
        entityColumn = "store_id"
    )
    val store: StoreEntity
)
