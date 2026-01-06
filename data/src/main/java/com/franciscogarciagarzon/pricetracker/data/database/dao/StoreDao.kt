package com.franciscogarciagarzon.pricetracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.franciscogarciagarzon.pricetracker.data.database.entity.StoreEntity

/**
 * Acceso a datos (DAO) para la entidad Store.
 * Centraliza las operaciones sobre la tabla 'stores'. Se enfoca en
 * la recuperación y creación de comercios, asegurando que el sistema pueda
 * identificar tiendas existentes antes de crear nuevas.
 */
@Dao
interface StoreDao {
    /**
     * Busca una tienda por nombre ignorando mayúsculas/minúsculas.
     * Se utiliza LOWER(:name) para normalizar la búsqueda.
     * Esto evita que el usuario cree "Mercadona" y "mercadona" como
     * tiendas distintas, manteniendo la coherencia de los datos para futuras
     * comparaciones de precios.
     */
    @Query("SELECT * FROM stores WHERE LOWER(name) = LOWER(:name) LIMIT 1")
    suspend fun getStoreByName(name: String): StoreEntity?

    /**
     * Recupera una tienda por su identificador único técnico.
     */
    @Query("SELECT * FROM stores WHERE store_id = :id LIMIT 1")
    suspend fun getStoreById(id: Long): StoreEntity?

    /**
     * Inserta una nueva tienda.
     * @return El ID generado por la base de datos (dbId).
     */
    @Insert
    suspend fun insertStore(store: StoreEntity): Long

    /**
     * Devuelve el número total de tiendas registradas.
     * Útil para métricas de depuración o para verificar si la base
     * de datos está vacía.
     */
    @Query("SELECT COUNT(*) FROM stores")
    suspend fun getCount(): Int
}