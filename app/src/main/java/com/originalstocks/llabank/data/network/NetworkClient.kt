package com.originalstocks.llabank.data.network

import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object NetworkClient {

    const val HEADER_API_KEY = "x-api-key"
    const val HEADER_Authorization = "Authorization"
    const val HEADER_ACCESS_TOKEN = "x-access-token"
    const val HEADER_USER_ID = "x-user-id"
    const val HEADER_USER_AGENT = "x-extended-user-agent"
    const val CONTENT_TYPE = "Content-Type"
    const val SESSION_TOKEN = "session-token"
    private const val NETWORK_CALL_TIMEOUT = 60

    fun create(
        baseUrl: String,
        cacheDir: File,
        cacheSize: Long
    ): RequestInterface {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .cache(Cache(cacheDir, cacheSize))
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
            .readTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(FlowCallAdapterFactory.create())
            .build()
            .create(RequestInterface::class.java)
    }

}