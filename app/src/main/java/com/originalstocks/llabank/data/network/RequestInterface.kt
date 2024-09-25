package com.originalstocks.llabank.data.network

import com.originalstocks.llabank.data.models.HotelsDataModel
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface RequestInterface {

    @GET(Endpoints.FETCH_HOTELS)
    fun getAllHotelsNetworkRequest(): Flow<HotelsDataModel>
}