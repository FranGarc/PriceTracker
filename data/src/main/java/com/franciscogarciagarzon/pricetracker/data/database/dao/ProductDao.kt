package com.franciscogarciagarzon.pricetracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.franciscogarciagarzon.pricetracker.data.database.entity.ProductEntity


@Dao
interface ProductDao {
    /**
     * Inserta un nuevo producto en la tabla y devuelve el ID generado.
     */
    @Insert
    suspend fun insertProduct(product: ProductEntity): Long

    /**
     * Busca un producto por su nombre y formato de unidad.
     * Esto es clave para determinar si el producto ya existe.
     */
    @Query("SELECT * FROM products WHERE LOWER(name) = LOWER(:name) AND unitFormat = :unitFormat LIMIT 1")
    suspend fun getProductByNameAndUnit(name: String, unitFormat: String): ProductEntity?

    @Query("SELECT * FROM products WHERE product_id = :id  LIMIT 1")
    suspend fun getProductById(id: Long): ProductEntity?

    @Query("SELECT * FROM products WHERE LOWER(name) = LOWER(:name) LIMIT 1")
    suspend fun getProductByName(name: String): ProductEntity?


    @Query("SELECT COUNT(*) FROM products")
    suspend fun getCount(): Int
}
