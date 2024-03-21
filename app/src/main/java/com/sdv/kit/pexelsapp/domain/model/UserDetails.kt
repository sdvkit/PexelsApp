package com.sdv.kit.pexelsapp.domain.model

data class UserDetails(
    var userId: String = "",
    var username: String? = "",
    var profilePictureUrl: String? = "",
    var phoneNumber: String? = "",
    var email: String = "",
    var fcmToken: String = "",
    var friends: List<UserDetails> = emptyList()
)