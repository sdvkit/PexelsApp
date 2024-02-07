package com.sdv.kit.pexelsapp.domain.usecase.photo

import com.sdv.kit.pexelsapp.domain.manager.ImageManager
import com.sdv.kit.pexelsapp.domain.model.Photo
import javax.inject.Inject

class DownloadPhotoImage @Inject constructor(
    private val imageManager: ImageManager
) {

    operator fun invoke(
        photo: Photo,
        onError: (Exception) -> Unit
    ) {
        imageManager.downloadImage(
            imageUrl = photo.src.original,
            imageName = photo.alt,
            onError = onError
        )
    }
}