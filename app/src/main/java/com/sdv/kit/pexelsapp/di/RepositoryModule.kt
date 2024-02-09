package com.sdv.kit.pexelsapp.di

import androidx.paging.Pager
import com.sdv.kit.pexelsapp.data.local.PexelsDatabaseClient
import com.sdv.kit.pexelsapp.data.remote.PexelsApi
import com.sdv.kit.pexelsapp.data.repository.BookmarkedRepositoryImpl
import com.sdv.kit.pexelsapp.data.repository.FeaturedCollectionRepositoryImpl
import com.sdv.kit.pexelsapp.data.repository.PhotoRepositoryImpl
import com.sdv.kit.pexelsapp.domain.model.FeaturedCollection
import com.sdv.kit.pexelsapp.domain.model.Photo
import com.sdv.kit.pexelsapp.domain.repository.BookmarkedRepository
import com.sdv.kit.pexelsapp.domain.repository.FeaturedCollectionRepository
import com.sdv.kit.pexelsapp.domain.repository.PhotoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideFeaturedCollectionRepository(
        featuredCollectionPager: Pager<Int, FeaturedCollection>,
        pexelsDatabaseClient: PexelsDatabaseClient,
        pexelsApi: PexelsApi
    ): FeaturedCollectionRepository = FeaturedCollectionRepositoryImpl(
        featuredCollectionPager = featuredCollectionPager,
        featuredCollectionDao = pexelsDatabaseClient.featuredCollectionDao(),
        pexelsApi = pexelsApi
    )

    @Provides
    @Singleton
    fun providePhotoRepository(
        photoPager: Pager<Int, Photo>,
        pexelsDatabaseClient: PexelsDatabaseClient,
        pexelsApi: PexelsApi
    ): PhotoRepository = PhotoRepositoryImpl(
        photoPager = photoPager,
        pexelsDatabaseClient = pexelsDatabaseClient,
        pexelsApi = pexelsApi
    )

    @Provides
    @Singleton
    fun provideBookmarkedRepository(
        pexelsDatabaseClient: PexelsDatabaseClient
    ): BookmarkedRepository = BookmarkedRepositoryImpl(
        pexelsDatabaseClient = pexelsDatabaseClient
    )
}