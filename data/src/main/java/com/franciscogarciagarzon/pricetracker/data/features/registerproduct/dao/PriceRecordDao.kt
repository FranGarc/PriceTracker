package com.franciscogarciagarzon.pricetracker.data.features.registerproduct.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.franciscogarciagarzon.pricetracker.data.database.entity.PriceRecordEntity

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
}