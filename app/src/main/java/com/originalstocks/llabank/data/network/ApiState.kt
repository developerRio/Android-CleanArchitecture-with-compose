package com.originalstocks.llabank.data.network


sealed class ApiState<out T>(val value: T? = null, val message: String? = null) {

    class Empty<T> : ApiState<T>()
    class Success<T>(data: T) : ApiState<T>(data)
    class Error(message: String) : ApiState<Nothing>()
    object Loading : ApiState<Nothing>()

}
