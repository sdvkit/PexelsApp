package com.sdv.kit.pexelsapp.data.remote.api

import com.sdv.kit.pexelsapp.data.remote.dto.NotificationRequest
import com.sdv.kit.pexelsapp.util.Constants
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FCMApi {

    @POST("send")
    @Headers("Authorization: key=${Constants.GOOGLE_FCM_KEY}")
    suspend fun sendNotification(
        @Body notificationRequest: NotificationRequest
    )
}