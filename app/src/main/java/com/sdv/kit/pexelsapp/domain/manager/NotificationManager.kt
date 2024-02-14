package com.sdv.kit.pexelsapp.domain.manager

import android.app.PendingIntent
import androidx.annotation.DrawableRes

interface NotificationManager {
    fun sendNotification(
        notificationId: Int,
        @DrawableRes icon: Int,
        title: String,
        text: String,
        autoCancel: Boolean,
        pendingIntent: PendingIntent?,
        isHighPriority: Boolean
    )
}