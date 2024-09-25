package com.originalstocks.llabank.data.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hotels", primaryKeys = ["id"])
data class HotelEntity(
    @ColumnInfo(name = "id", index = true)
    val id: String,
    @ColumnInfo(name = "avatar")
    val avatar: String?,
    @ColumnInfo(name = "createdAt")
    val createdAt: String?,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "name")
    val name: String?
)
