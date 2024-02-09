package com.sdv.kit.pexelsapp.domain.repository

import androidx.paging.PagingData
import com.sdv.kit.pexelsapp.domain.model.FeaturedCollection
import kotlinx.coroutines.flow.Flow

interface FeaturedCollectionRepository {
    fun getPagedFeaturedCollections(): Flow<PagingData<FeaturedCollection>>
    suspend fun getFeaturedCollections(page: Int): List<FeaturedCollection>
    fun checkIfCollectionsInCache(): Boolean
    suspend fun cacheFeaturedCollections(collections: List<FeaturedCollection>)
}