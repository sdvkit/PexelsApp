package com.sdv.kit.pexelsapp.domain.usecase.photo

import android.graphics.Bitmap
import com.sdv.kit.pexelsapp.domain.manager.ImageManager
import javax.inject.Inject

class GetLocalImagesByPrefix @Inject constructor(
    private val imageManager: ImageManager
) {

    operator fun invoke(prefix: String? = null): List<Bitmap> {
        return imageManager.getImagesFromPicturesFolder(prefix = prefix)
    }
}