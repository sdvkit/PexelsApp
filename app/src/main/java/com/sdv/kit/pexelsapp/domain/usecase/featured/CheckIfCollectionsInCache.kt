package com.sdv.kit.pexelsapp.domain.usecase.featured

import com.sdv.kit.pexelsapp.domain.repository.FeaturedCollectionRepository
import javax.inject.Inject

class CheckIfCollectionsInCache @Inject constructor(
    private val featuredCollectionRepository: FeaturedCollectionRepository
) {

    operator fun invoke(): Boolean {
        return featuredCollectionRepository.checkIfCollectionsInCache()
    }
}