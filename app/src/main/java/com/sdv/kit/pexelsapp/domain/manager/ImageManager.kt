package com.sdv.kit.pexelsapp.domain.manager

import android.graphics.Bitmap

interface ImageManager {
    fun downloadImage(
        imageUrl: String,
        imageName: String,
        onError: (Exception) -> Unit
    )

    fun saveImageToGallery(bitmap: Bitmap, imageName: String)

    fun getImagesFromPicturesFolder(prefix: String?): List<Bitmap>
}