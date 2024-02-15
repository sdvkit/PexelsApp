package com.sdv.kit.pexelsapp.presentation.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.data.manager.NotificationManagerImpl
import com.sdv.kit.pexelsapp.presentation.navigation.NavGraph
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme
import com.sdv.kit.pexelsapp.util.NotificationConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition(condition = {
                viewModel.keepOnSplashScreen.value
            })
        }

        handleNotificationsPermission()

        setContent {
            AppTheme {
                NavGraph(startDestination = viewModel.startDestination.value)

                if (viewModel.fcmToken.value.isNotBlank()) {
                    sendFCMNotification()
                }
            }
        }
    }

    private fun handleNotificationsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_NOTIFICATION_PERMISSION_CODE
                )
            }
        }
    }

    private fun sendFCMNotification() {
        val notificationManager = NotificationManagerImpl(context = this)

        notificationManager.sendNotification(
            notificationId = NotificationConstants.NOTIFICATION_FCM_TOKEN_ID,
            icon = R.drawable.ic_logo,
            title = applicationContext.getString(R.string.fcm_token_success_title),
            text = applicationContext.getString(R.string.fcm_token_success_text),
            autoCancel = true,
            pendingIntent = null,
            isHighPriority = true
        )
    }

    companion object {
        private const val REQUEST_NOTIFICATION_PERMISSION_CODE = 101
    }
}