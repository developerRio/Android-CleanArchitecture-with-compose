package com.originalstocks.llabank.data.di

import android.content.Context
import androidx.room.Room
import com.originalstocks.llabank.BuildConfig
import com.originalstocks.llabank.data.cache.HotelsDao
import com.originalstocks.llabank.data.cache.HotelsDatabase
import com.originalstocks.llabank.data.network.NetworkClient
import com.originalstocks.llabank.data.network.RequestInterface
import com.originalstocks.llabank.data.repository.DashboardRepository
import com.originalstocks.llabank.utils.Utils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

interface StandardDispatchers {
    val ioDispatcher: CoroutineDispatcher
    val defaultDispatcher: CoroutineDispatcher
    val mainDispatcher: CoroutineDispatcher
    val unconfinedDispatcher: CoroutineDispatcher
}

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Provides
    @Singleton
    fun provideStandardDispatchers(): StandardDispatchers = object : StandardDispatchers {
        override val ioDispatcher: CoroutineDispatcher
            get() = Dispatchers.IO
        override val defaultDispatcher: CoroutineDispatcher
            get() = Dispatchers.Default
        override val mainDispatcher: CoroutineDispatcher
            get() = Dispatchers.Main
        override val unconfinedDispatcher: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }

    @Singleton
    @Provides
    fun providesNetworkService(@ApplicationContext context: Context): RequestInterface =
        NetworkClient.create(
            baseUrl = BuildConfig.BASE_URL,
            cacheDir = context.cacheDir,
            cacheSize = Utils.RETRO_CACHE_SIZE
        )

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HotelsDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            HotelsDatabase::class.java,
            "hotel_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideHotelDao(database: HotelsDatabase): HotelsDao {
        return database.hotelsDao()
    }

    @Module
    @InstallIn(ViewModelComponent::class)
    internal object RepositoryModule {

        @Provides
        @ViewModelScoped
        fun provideDashboardRepository(requestInterface: RequestInterface, hotelsDao: HotelsDao) = DashboardRepository(requestInterface, hotelsDao)

    }
}