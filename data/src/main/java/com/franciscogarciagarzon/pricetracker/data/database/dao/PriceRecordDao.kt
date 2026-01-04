package com.franciscogarciagarzon.pricetracker.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.franciscogarciagarzon.pricetracker.data.database.entity.PriceRecordEntity
import com.franciscogarciagarzon.pricetracker.data.database.model.PriceRecordWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface PriceRecordDao {

    @Query("SELECT * FROM pricerecords WHERE product_id = :productId ")
    suspend fun getPriceRecordByProductId(productId: Long): List<PriceRecordEntity>

    @Query("SELECT * FROM pricerecords WHERE dbId = :priceRecordId ")
    suspend fun getPriceRecordById(priceRecordId: Long): PriceRecordEntity?


    @Insert
    suspend fun insertPriceRecord(record: PriceRecordEntity): Long

    @Query("SELECT COUNT(*) FROM pricerecords WHERE product_id = :productId ")
    suspend fun getRecordCountForProduct(productId: Long): Int

    @Query("SELECT * FROM pricerecords ")
    suspend fun getAll(): List<PriceRecordEntity>

    @Delete
    suspend fun delete(record: PriceRecordEntity)

    @Transaction // "bloquea" la base de datos para que los datos que lee sean consistentes entre sí en ese instante preciso
    // lo necesitamos por ser una consulta a múltiples tablas, DELETE, INSERT y UPDATE ya son transaccionales de por sí
    @Query("SELECT * FROM pricerecords ORDER BY purchase_date DESC LIMIT 5")
    fun getRecentRecordsWithDetails(): Flow<List<PriceRecordWithDetails>>
}