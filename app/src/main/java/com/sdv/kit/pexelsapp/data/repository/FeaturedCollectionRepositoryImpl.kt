package com.sdv.kit.pexelsapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.sdv.kit.pexelsapp.data.local.dao.FeaturedCollectionDao
import com.sdv.kit.pexelsapp.domain.model.FeaturedCollection
import com.sdv.kit.pexelsapp.domain.repository.FeaturedCollectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FeaturedCollectionRepositoryImpl @Inject constructor(
    private val featuredCollectionPager: Pager<Int, FeaturedCollection>,
    private val featuredCollectionDao: FeaturedCollectionDao
) : FeaturedCollectionRepository {

    override fun getFeaturedCollections(): Flow<PagingData<FeaturedCollection>> {
        return featuredCollectionPager.flow
    }

    override fun checkIfCollectionsInCache(): Boolean {
        return featuredCollectionDao.getCount() > 0
    }
}