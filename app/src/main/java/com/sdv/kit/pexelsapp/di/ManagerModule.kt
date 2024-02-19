package com.sdv.kit.pexelsapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.work.WorkManager
import com.sdv.kit.pexelsapp.data.manager.GoogleAuthManagerImpl
import com.sdv.kit.pexelsapp.data.manager.ImageManagerImpl
import com.sdv.kit.pexelsapp.data.manager.PermissionEntryManagerImpl
import com.sdv.kit.pexelsapp.domain.manager.GoogleAuthManager
import com.sdv.kit.pexelsapp.domain.manager.ImageManager
import com.sdv.kit.pexelsapp.domain.manager.PermissionEntryManager
import com.sdv.kit.pexelsapp.util.Constants
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
    fun provideSharedPref(
        application: Application
    ): SharedPreferences = application.getSharedPreferences(
        Constants.SHARED_PREF_NAME,
        Context.MODE_PRIVATE
    )

    @Provides
    @Singleton
    fun permissionEntryManager(
        sharedPreferences: SharedPreferences
    ): PermissionEntryManager = PermissionEntryManagerImpl(
        sharedPreferences = sharedPreferences
    )

    @Provides
    @Singleton
    fun provideImageManager(
        application: Application
    ): ImageManager = ImageManagerImpl(
        context = application
    )

    @Provides
    @Singleton
    fun provideGoogleAuthManager(
        application: Application
    ): GoogleAuthManager = GoogleAuthManagerImpl(
        context = application
    )

    @Provides
    @Singleton
    fun provideWorkManager(
        application: Application
    ): WorkManager = WorkManager.getInstance(application)
}