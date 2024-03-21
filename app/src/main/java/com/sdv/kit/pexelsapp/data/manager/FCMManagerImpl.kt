package com.sdv.kit.pexelsapp.data.manager

import android.content.SharedPreferences
import com.sdv.kit.pexelsapp.domain.manager.FCMManager
import javax.inject.Inject

class FCMManagerImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : FCMManager {

    override fun saveToken(token: String) {
        sharedPreferences.edit()
            .putString(FCM_KEY, token)
            .apply()
    }

    override fun readToken(): String {
        return sharedPreferences.getString(FCM_KEY, DEFAULT_FCM_VALUE) ?: ""
    }

    companion object {
        private const val FCM_KEY = "fcm_token"
        private const val DEFAULT_FCM_VALUE = ""
    }
}