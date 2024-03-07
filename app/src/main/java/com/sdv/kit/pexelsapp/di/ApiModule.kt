package com.sdv.kit.pexelsapp.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.sdv.kit.pexelsapp.data.local.PexelsDatabaseClient
import com.sdv.kit.pexelsapp.data.paging.mediator.FeaturedCollectionRemoteMediator
import com.sdv.kit.pexelsapp.data.paging.mediator.PhotoRemoteMediator
import com.sdv.kit.pexelsapp.data.remote.api.FCMApi
import com.sdv.kit.pexelsapp.data.remote.api.PexelsApi
import com.sdv.kit.pexelsapp.data.remote.client.FCMApiClient
import com.sdv.kit.pexelsapp.data.remote.client.PexelsApiClient
import com.sdv.kit.pexelsapp.domain.model.FeaturedCollection
import com.sdv.kit.pexelsapp.domain.model.Photo
import com.sdv.kit.pexelsapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun providePexelsApiClient(): PexelsApiClient =
        PexelsApiClient

    @Provides
    @Singleton
    fun provideFCMApiClient(): FCMApiClient =
        FCMApiClient

    @Provides
    @Singleton
    fun providePexelsApi(
        apiClient: PexelsApiClient
    ): PexelsApi = apiClient.client.create()

    @Provides
    @Singleton
    fun provideFCMApi(
        apiClient: FCMApiClient
    ): FCMApi = apiClient.client.create()

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideFeaturedCollectionPager(
        pexelsDatabaseClient: PexelsDatabaseClient,
        pexelsApi: PexelsApi
    ): Pager<Int, FeaturedCollection> {
        return Pager(
            config = PagingConfig(pageSize = Constants.PAGE_CONFIG_FEATURED_SIZE),
            remoteMediator = FeaturedCollectionRemoteMediator(
                pexelsDatabaseClient = pexelsDatabaseClient,
                pexelsApi = pexelsApi
            ),
            pagingSourceFactory = {
                pexelsDatabaseClient.featuredCollectionDao().pagingSource()
            }
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun providePhotoPager(
        pexelsDatabaseClient: PexelsDatabaseClient,
        pexelsApi: PexelsApi
    ): Pager<Int, Photo> {
        return Pager(
            config = PagingConfig(pageSize = Constants.PAGE_CONFIG_PHOTO_SIZE),
            remoteMediator = PhotoRemoteMediator(
                pexelsDatabaseClient = pexelsDatabaseClient,
                pexelsApi = pexelsApi
            ),
            pagingSourceFactory = {
                pexelsDatabaseClient.photoDao().pagingSource()
            }
        )
    }
}