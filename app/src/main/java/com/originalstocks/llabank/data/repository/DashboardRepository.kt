package com.originalstocks.llabank.data.repository

import com.originalstocks.llabank.data.cache.HotelEntity
import com.originalstocks.llabank.data.cache.HotelsDao
import com.originalstocks.llabank.data.network.RequestInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val requestInterface: RequestInterface,
    private val hotelsDao: HotelsDao
) {

    suspend fun getHotels(): Flow<List<HotelEntity>> = flow {
        val cachedHotels = hotelsDao.getHotels().first()
        if (cachedHotels.isNotEmpty()) {
            emit(cachedHotels)  // Return from cache
        } else {
            val response = requestInterface.getAllHotelsNetworkRequest().first()
            // Insert into database
            val hotelsToCache = response.map {
                HotelEntity(
                    id = it.id ?: "",
                    avatar = it.avatar,
                    createdAt = it.createdAt,
                    description = it.description,
                    name = it.name
                )
            }
            hotelsDao.insertHotels(hotelsToCache)
            emit(hotelsToCache)  // Return fresh data
        }
    }

    /*suspend fun clearCache() {
        hotelsDao.clearHotels()
    }*/
}