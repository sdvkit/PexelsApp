package com.sdv.kit.pexelsapp.domain.manager

interface ImageManager {
    fun downloadImage(
        imageUrl: String,
        imageName: String,
        onError: (Exception) -> Unit
    )
}