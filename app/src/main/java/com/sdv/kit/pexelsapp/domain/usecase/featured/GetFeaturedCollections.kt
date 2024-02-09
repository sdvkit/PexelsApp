package com.sdv.kit.pexelsapp.domain.usecase.featured

import com.sdv.kit.pexelsapp.domain.model.FeaturedCollection
import com.sdv.kit.pexelsapp.domain.repository.FeaturedCollectionRepository
import javax.inject.Inject

class GetFeaturedCollections @Inject constructor(
    private val featuredCollectionRepository: FeaturedCollectionRepository
) {

    suspend operator fun invoke(page: Int): List<FeaturedCollection> {
        return featuredCollectionRepository.getFeaturedCollections(page = page)
    }
}