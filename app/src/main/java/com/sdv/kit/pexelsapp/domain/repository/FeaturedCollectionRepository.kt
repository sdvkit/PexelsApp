package com.sdv.kit.pexelsapp.domain.repository

import androidx.paging.PagingData
import com.sdv.kit.pexelsapp.domain.model.FeaturedCollection
import kotlinx.coroutines.flow.Flow

interface FeaturedCollectionRepository {
    fun getFeaturedCollections(): Flow<PagingData<FeaturedCollection>>
    fun checkIfCollectionsInCache(): Boolean
}