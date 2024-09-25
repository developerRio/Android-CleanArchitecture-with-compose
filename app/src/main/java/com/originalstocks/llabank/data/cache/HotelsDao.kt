package com.originalstocks.llabank.data.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.originalstocks.llabank.data.models.HotelsDataModel
import kotlinx.coroutines.flow.Flow

@Dao
interface HotelsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHotels(hotels: List<HotelEntity>)

    @Query("SELECT * FROM hotels")
    fun getHotels(): Flow<List<HotelEntity>>

    /*@Query("DELETE FROM hotels")
    suspend fun clearHotels(): Int*/
}