package com.sdv.kit.pexelsapp.domain.model

data class UserDetails(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?,
    val phoneNumber: String?,
    val email: String
)