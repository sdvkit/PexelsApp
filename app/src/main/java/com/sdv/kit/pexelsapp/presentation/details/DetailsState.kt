package com.sdv.kit.pexelsapp.presentation.details

import com.sdv.kit.pexelsapp.domain.model.Photo

data class DetailsState(
    val photo: Photo? = null,
    val isBookmarked: Boolean = false,
    val isPhotoLoading: Boolean = false,
    val photoError: Throwable? = null
)