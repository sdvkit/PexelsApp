package com.sdv.kit.pexelsapp.presentation.worker

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.data.manager.NotificationManagerImpl
import com.sdv.kit.pexelsapp.presentation.activity.MainActivity
import com.sdv.kit.pexelsapp.util.NotificationConstants

class DailyCheckPhotosWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val notificationManager = NotificationManagerImpl(context = applicationContext)
        val pendingIntent = buildPendingIntent()

        notificationManager.sendNotification(
            notificationId = NotificationConstants.NOTIFICATION_DAILY_PHOTO_ID,
            icon = R.drawable.ic_logo,
            title = applicationContext.getString(R.string.daily_photos_notification_title),
            text = applicationContext.getString(R.string.daily_photos_notification_text),
            autoCancel = true,
            pendingIntent = pendingIntent,
            isHighPriority = false
        )

        return Result.success()
    }

    private fun buildPendingIntent(): PendingIntent {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        return PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }
}