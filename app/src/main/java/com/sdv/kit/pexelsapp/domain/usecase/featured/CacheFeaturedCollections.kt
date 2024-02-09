package com.sdv.kit.pexelsapp.domain.usecase.featured

import com.sdv.kit.pexelsapp.domain.model.FeaturedCollection
import com.sdv.kit.pexelsapp.domain.repository.FeaturedCollectionRepository
import javax.inject.Inject

class CacheFeaturedCollections @Inject constructor(
    private val featuredCollectionRepository: FeaturedCollectionRepository
) {

    suspend operator fun invoke(collections: List<FeaturedCollection>) {
        featuredCollectionRepository.cacheFeaturedCollections(collections = collections)
    }
}