package com.franciscogarciagarzon.pricetracker.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.franciscogarciagarzon.pricetracker.data.database.entity.PriceRecordEntity
import com.franciscogarciagarzon.pricetracker.data.database.model.PriceRecordWithDetails
import kotlinx.coroutines.flow.Flow

/**
 * Acceso a datos (DAO) para los registros de precios.
 * Gestiona el histórico de compras y orquestas la recuperación de datos
 * relacionales. Es el nexo de unión entre los productos, las tiendas y los precios.
 */
@Dao
interface PriceRecordDao {

    /**
     * Recupera registros de compra para el producto cuya clave primaria sea productId.
     */
    @Query("SELECT * FROM pricerecords WHERE product_id = :productId ")
    suspend fun getPriceRecordByProductId(productId: Long): List<PriceRecordEntity>

    /**
     * Recupera un registro de compra por su clave primaria.
     */
    @Query("SELECT * FROM pricerecords WHERE dbId = :priceRecordId ")
    suspend fun getPriceRecordById(priceRecordId: Long): PriceRecordEntity?


    /**
     * Inserta un nuevo registro de compra.
     * @return El ID generado (priceRecordId) para ser usado como clave foránea en los registros de precio.
     */
    @Insert
    suspend fun insertPriceRecord(record: PriceRecordEntity): Long

    /**
     * Devuelve el número total de registros de compra para el producto cuya clave primaria sea productId.
     */
    @Query("SELECT COUNT(*) FROM pricerecords WHERE product_id = :productId ")
    suspend fun getRecordCountForProduct(productId: Long): Int

    /**
     * Devuelve todos los registros de compra registrados.
     */
    @Query("SELECT * FROM pricerecords ")
    suspend fun getAll(): List<PriceRecordEntity>

    /**
     * Elimina un registro de compra.
     */
    @Delete
    suspend fun delete(record: PriceRecordEntity)

    /**
     * Obtiene los últimos 5 registros de compra con toda su información asociada.
     * Se usa @Transaction porque Room realiza múltiples consultas bajo el
     * capó para llenar los objetos @Relation (Product y Store). La transacción asegura
     * una "foto" atómica de la base de datos, evitando inconsistencias.
     * Al devolver Flow, la UI se actualizará automáticamente en cuanto se
     * inserte un nuevo registro sin necesidad de recarga manual.
     */
    @Transaction
    @Query("SELECT * FROM pricerecords ORDER BY purchase_date DESC LIMIT 5")
    fun getRecentRecordsWithDetails(): Flow<List<PriceRecordWithDetails>>
}