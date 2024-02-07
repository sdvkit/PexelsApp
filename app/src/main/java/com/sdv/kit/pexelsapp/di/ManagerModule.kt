package com.sdv.kit.pexelsapp.di

import android.app.Application
import com.sdv.kit.pexelsapp.data.manager.ImageManagerImpl
import com.sdv.kit.pexelsapp.domain.manager.ImageManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ManagerModule {

    @Provides
    @Singleton
    fun provideImageManager(
        application: Application
    ): ImageManager = ImageManagerImpl(
        context = application
    )
}