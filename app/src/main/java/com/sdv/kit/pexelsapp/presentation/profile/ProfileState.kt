package com.sdv.kit.pexelsapp.presentation.profile

import com.sdv.kit.pexelsapp.domain.model.UserDetails

data class ProfileState(
    val userDetails: UserDetails? = null,
    val isSignedOut: Boolean = false
)