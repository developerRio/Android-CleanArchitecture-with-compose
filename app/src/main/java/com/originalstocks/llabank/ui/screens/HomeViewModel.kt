package com.originalstocks.llabank.ui.screens

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.originalstocks.llabank.R
import com.originalstocks.llabank.data.cache.HotelEntity
import com.originalstocks.llabank.data.models.HotelsDataModel
import com.originalstocks.llabank.data.network.ApiState
import com.originalstocks.llabank.data.repository.DashboardRepository
import com.originalstocks.llabank.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: DashboardRepository
) : BaseViewModel() {

    var hotelResponseStateFlow: MutableStateFlow<ApiState<List<HotelEntity>>> =
        MutableStateFlow(ApiState.Empty())

    var searchQuery = mutableStateOf("")

    fun updateSearchQuery(newQuery: String) {
        searchQuery.value = newQuery
    }

    fun tabulatingAnalyticsForBottomSheet(value: List<HotelEntity>): String {

        val tempList: ArrayList<String> = ArrayList()
        val strBuilder = StringBuilder()

        for (hotel in value) {
            tempList.add(hotel.name?.trim() ?: "")
        }

        val combinedStr = tempList.joinToString("")

        val charOccurrence = combinedStr.filter { it.isLetter() }.groupingBy { it }.eachCount()

        val topChars = charOccurrence.entries.sortedByDescending { it.value }.take(3)
        for ((char, count) in topChars) {
            Log.e(TAG, "tabulatingAnalyticsForBottomSheet: $char: $count")
            strBuilder.append("$char:").append("$count").append("\n")
        }

        return strBuilder.toString()
    }

    fun getAllHotelsNetworkRequest() {
        scope.launch(Dispatchers.IO) {
            showLoader()
            Log.e(TAG, "getAllHotelsNetworkRequest: loading")
            repository.getHotels().catch { exception ->
                hideLoader()
                Log.e(TAG, "getAllHotelsNetworkRequest: exception -> $exception")
                hotelResponseStateFlow.value = ApiState.Error(exception.toString())
            }.collect { response ->
                hideLoader()
                Log.w(TAG, "getAllHotelsNetworkRequest: response -> $response")
                hotelResponseStateFlow.value = ApiState.Success(data = response)
            }
        }
    }

    fun moveToDashboardPage() {
        navigateTo(
            navigationRouteId = R.id.action_splashFragment_to_homeFragment,
            isInclusive = true,
            optionalPopUpToId = R.id.splashFragment,
        )
    }
}