package com.sdv.kit.pexelsapp.presentation.profile

import android.graphics.Bitmap
import com.sdv.kit.pexelsapp.domain.model.UserDetails

data class ProfileState(
    val userDetails: UserDetails? = null,
    val isSignedOut: Boolean = false,
    val localImages: List<Bitmap> = listOf()
)