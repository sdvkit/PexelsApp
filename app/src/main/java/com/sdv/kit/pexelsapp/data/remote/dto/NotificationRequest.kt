package com.sdv.kit.pexelsapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class Notification(

    @SerializedName("title")
    val title: String,

    @SerializedName("body")
    val body: String,
)

data class NotificationRequest(

    @SerializedName("to")
    val to: String,

    val notification: Notification
)