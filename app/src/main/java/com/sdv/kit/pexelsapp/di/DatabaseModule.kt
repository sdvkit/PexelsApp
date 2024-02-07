package com.sdv.kit.pexelsapp.di

import android.app.Application
import androidx.room.Room
import com.sdv.kit.pexelsapp.data.local.PexelsDatabaseClient
import com.sdv.kit.pexelsapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun providePexelsDatabaseClient(
        application: Application
    ): PexelsDatabaseClient = Room
        .databaseBuilder(
            context = application,
            klass = PexelsDatabaseClient::class.java,
            name = Constants.DB_NAME
        )
        .fallbackToDestructiveMigration()
        .build()
}