package com.sdv.kit.pexelsapp.domain.usecase.fcm

import com.sdv.kit.pexelsapp.data.remote.api.FCMApi
import com.sdv.kit.pexelsapp.data.remote.dto.NotificationRequest
import javax.inject.Inject

class SendNotification @Inject constructor(
    private val fcmApi: FCMApi
) {

    suspend operator fun invoke(notificationRequest: NotificationRequest) {
        fcmApi.sendNotification(notificationRequest = notificationRequest)
    }
}