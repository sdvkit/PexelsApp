package com.sdv.kit.pexelsapp.presentation.camera

import android.graphics.Bitmap

data class CameraState(
    val lastBitmap: Bitmap? = null,
    val errorMessage: String? = null,
    val lastPermissionEntry: Pair<String, Boolean>? = null
)