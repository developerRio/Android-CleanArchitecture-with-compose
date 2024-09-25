package com.originalstocks.llabank.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.originalstocks.llabank.data.models.HotelsDataModel

@Database(entities = [HotelEntity::class], version = 1, exportSchema = false)
abstract class HotelsDatabase: RoomDatabase() {
    abstract fun hotelsDao(): HotelsDao
}