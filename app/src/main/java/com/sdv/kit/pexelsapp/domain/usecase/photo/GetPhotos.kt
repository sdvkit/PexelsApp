package com.sdv.kit.pexelsapp.domain.usecase.photo

import com.sdv.kit.pexelsapp.domain.model.Photo
import com.sdv.kit.pexelsapp.domain.repository.PhotoRepository
import javax.inject.Inject

class GetPhotos @Inject constructor(
    private val photoRepository: PhotoRepository
) {

    suspend operator fun invoke(page: Int): List<Photo> {
        return photoRepository.getPhotos(page = page)
    }
}