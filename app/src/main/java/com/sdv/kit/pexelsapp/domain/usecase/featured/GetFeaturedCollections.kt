package com.sdv.kit.pexelsapp.domain.usecase.featured

import androidx.paging.PagingData
import com.sdv.kit.pexelsapp.domain.model.FeaturedCollection
import com.sdv.kit.pexelsapp.domain.repository.FeaturedCollectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFeaturedCollections @Inject constructor(
    private val featuredCollectionRepository: FeaturedCollectionRepository
) {

    operator fun invoke(): Flow<PagingData<FeaturedCollection>> {
        return featuredCollectionRepository.getFeaturedCollections()
    }
}