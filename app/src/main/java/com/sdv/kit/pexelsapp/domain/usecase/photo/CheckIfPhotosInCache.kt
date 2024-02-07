package com.sdv.kit.pexelsapp.domain.usecase.photo

import com.sdv.kit.pexelsapp.domain.repository.PhotoRepository
import javax.inject.Inject

class CheckIfPhotosInCache @Inject constructor(
    private val photoRepository: PhotoRepository
) {

    operator fun invoke(): Boolean {
        return photoRepository.checkIfPhotosInCache()
    }
}