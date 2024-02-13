package com.sdv.kit.pexelsapp.presentation.worker

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.data.manager.NotificationManagerImpl

class ChargingWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val notificationManager = NotificationManagerImpl(context = applicationContext)
        val pendingIntent = buildPendingIntent()

        notificationManager.sendNotification(
            notificationId = 1,
            icon = R.drawable.ic_logo,
            title = applicationContext.getString(R.string.charge_notification_title),
            text = applicationContext.getString(R.string.charge_notification_text),
            autoCancel = true,
            pendingIntent = pendingIntent
        )

        return Result.success()
    }

    private fun buildPendingIntent(): PendingIntent {
        val intent = Intent()
        return PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }
}