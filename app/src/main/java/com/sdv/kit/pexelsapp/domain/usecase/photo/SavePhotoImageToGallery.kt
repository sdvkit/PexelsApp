package com.sdv.kit.pexelsapp.domain.usecase.photo

import android.graphics.Bitmap
import com.sdv.kit.pexelsapp.domain.manager.ImageManager
import com.sdv.kit.pexelsapp.util.Constants
import javax.inject.Inject

class SavePhotoImageToGallery @Inject constructor(
    private val imageManager: ImageManager
) {

    operator fun invoke(bitmap: Bitmap) {
        imageManager.saveImageToGallery(
            bitmap = bitmap,
            imageName = "${Constants.IMAGE_NAME_PREFIX}_${System.currentTimeMillis()}"
        )
    }
}