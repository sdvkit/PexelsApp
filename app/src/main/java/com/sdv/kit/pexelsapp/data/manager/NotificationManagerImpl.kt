package com.sdv.kit.pexelsapp.data.manager

import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import com.sdv.kit.pexelsapp.domain.manager.NotificationManager
import com.sdv.kit.pexelsapp.util.NotificationConstants

class NotificationManagerImpl(private val context: Context) : NotificationManager {

    override fun sendNotification(
        notificationId: Int,
        icon: Int,
        title: String,
        text: String,
        autoCancel: Boolean,
        pendingIntent: PendingIntent?,
        isHighPriority: Boolean
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager

        val notificationChannel =
            NotificationChannel(
                NotificationConstants.CHANNEL_ID,
                NotificationConstants.CHANNEL_NAME,
                android.app.NotificationManager.IMPORTANCE_HIGH
            )

        notificationManager.createNotificationChannel(notificationChannel)

        val priority = if (isHighPriority) NotificationCompat.PRIORITY_HIGH else NotificationCompat.PRIORITY_DEFAULT
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat
            .Builder(context, NotificationConstants.CHANNEL_ID)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(autoCancel)
            .setPriority(priority)


        if (pendingIntent != null) {
            notificationBuilder.setFullScreenIntent(pendingIntent, isHighPriority)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}