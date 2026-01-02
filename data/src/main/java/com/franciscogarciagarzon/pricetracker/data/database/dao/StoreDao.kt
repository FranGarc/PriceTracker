package com.franciscogarciagarzon.pricetracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.franciscogarciagarzon.pricetracker.data.database.entity.StoreEntity

@Dao
interface StoreDao {
    @Query("SELECT * FROM stores WHERE LOWER(name) = LOWER(:name) LIMIT 1")
    suspend fun getStoreByName(name: String): StoreEntity?

    @Query("SELECT * FROM stores WHERE store_id = :id LIMIT 1")
    suspend fun getStoreById(id: Long): StoreEntity?

    @Insert
    suspend fun insertStore(store: StoreEntity): Long

    @Query("SELECT COUNT(*) FROM stores")
    suspend fun getCount(): Int
}