package com.franciscogarciagarzon.pricetracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.franciscogarciagarzon.pricetracker.data.database.entity.ProductEntity

/**
 * Acceso a datos (DAO) para la entidad Product.
 * Gestiona el catálogo de productos. Su función principal es permitir
 * la reutilización de productos existentes para mantener un historial de precios
 * consistente y limpio.
 */
@Dao
interface ProductDao {
    /**
     * Inserta un nuevo producto.
     * @return El ID generado (product_id) para ser usado como clave foránea en los registros de precio.
     */
    @Insert
    suspend fun insertProduct(product: ProductEntity): Long

    /**
     * Busca un producto por nombre y unidad (ignora mayúsculas).
     * Esta consulta es el corazón de la lógica de duplicados.
     * Al filtrar por 'unitFormat', permitimos que el mismo nombre tenga diferentes
     * entradas si la unidad cambia (ej. "Detergente" en Gramos vs "Detergente" en Litros).
     */
    @Query("SELECT * FROM products WHERE LOWER(name) = LOWER(:name) AND unitFormat = :unitFormat LIMIT 1")
    suspend fun getProductByNameAndUnit(name: String, unitFormat: String): ProductEntity?

    /**
     * Recupera un producto por su clave primaria.
     */
    @Query("SELECT * FROM products WHERE product_id = :id  LIMIT 1")
    suspend fun getProductById(id: Long): ProductEntity?

    /**
     * Busca por nombre sin considerar la unidad.
     * Útil para sugerencias de autocompletado o búsquedas generales de productos.
     */
    @Query("SELECT * FROM products WHERE LOWER(name) = LOWER(:name) LIMIT 1")
    suspend fun getProductByName(name: String): ProductEntity?


    /**
     * Devuelve el número total de productos registrados.
     * Útil para métricas de depuración o para verificar si la base
     * de datos está vacía.
     */
    @Query("SELECT COUNT(*) FROM products")
    suspend fun getCount(): Int
}
