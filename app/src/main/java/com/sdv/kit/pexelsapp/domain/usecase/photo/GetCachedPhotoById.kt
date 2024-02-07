package com.sdv.kit.pexelsapp.domain.usecase.photo

import com.sdv.kit.pexelsapp.domain.model.Photo
import com.sdv.kit.pexelsapp.domain.repository.PhotoRepository
import javax.inject.Inject

class GetCachedPhotoById @Inject constructor(
    private val photoRepository: PhotoRepository
) {

    suspend operator fun invoke(photoId: Int): Photo {
        return photoRepository.getCachedPhotoById(photoId = photoId)
    }
}