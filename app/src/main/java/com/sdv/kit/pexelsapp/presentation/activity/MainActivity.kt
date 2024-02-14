package com.sdv.kit.pexelsapp.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.data.manager.NotificationManagerImpl
import com.sdv.kit.pexelsapp.presentation.navigation.NavGraph
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme
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

        setContent {
            AppTheme {
                NavGraph(startDestination = viewModel.startDestination.value)

                if (viewModel.fcmToken.value.isNotBlank()) {
                    sendFCMNotification()
                }
            }
        }
    }

    private fun sendFCMNotification() {
        val notificationManager = NotificationManagerImpl(context = this)

        notificationManager.sendNotification(
            notificationId = 3,
            icon = R.drawable.ic_logo,
            title = applicationContext.getString(R.string.fcm_token_success_title),
            text = applicationContext.getString(R.string.fcm_token_success_text),
            autoCancel = true,
            pendingIntent = null,
            isHighPriority = true
        )
    }
}