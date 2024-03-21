package com.sdv.kit.pexelsapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.sdv.kit.pexelsapp.data.local.dao.FeaturedCollectionDao
import com.sdv.kit.pexelsapp.data.remote.api.PexelsApi
import com.sdv.kit.pexelsapp.domain.model.FeaturedCollection
import com.sdv.kit.pexelsapp.domain.repository.FeaturedCollectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FeaturedCollectionRepositoryImpl @Inject constructor(
    private val featuredCollectionPager: Pager<Int, FeaturedCollection>,
    private val featuredCollectionDao: FeaturedCollectionDao,
    private val pexelsApi: PexelsApi
) : FeaturedCollectionRepository {

    override fun getPagedFeaturedCollections(): Flow<PagingData<FeaturedCollection>> {
        return featuredCollectionPager.flow
    }

    override suspend fun getFeaturedCollections(page: Int): List<FeaturedCollection> {
        return pexelsApi.getFeatured(page = page).featuredCollections
    }

    override fun checkIfCollectionsInCache(): Boolean {
        return featuredCollectionDao.getCount() > 0
    }

    override suspend fun cacheFeaturedCollections(collections: List<FeaturedCollection>) {
        featuredCollectionDao.insertAll(*collections.toTypedArray())
    }
}