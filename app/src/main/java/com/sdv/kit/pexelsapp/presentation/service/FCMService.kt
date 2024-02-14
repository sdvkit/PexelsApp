package com.sdv.kit.pexelsapp.presentation.service

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sdv.kit.pexelsapp.PexelsApplication

class FCMService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.i(LOG_TAG, token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if (PexelsApplication.isAppVisible) {
            showToastMessage(message = message)
        }
    }

    private fun showToastMessage(message: RemoteMessage) {
        Handler(Looper.getMainLooper()).post {
            val messageText = message.notification?.body ?: ""
            Toast.makeText(applicationContext, messageText, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val LOG_TAG = "UI"
    }
}