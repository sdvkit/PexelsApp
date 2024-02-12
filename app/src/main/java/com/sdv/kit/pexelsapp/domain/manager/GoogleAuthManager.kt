package com.sdv.kit.pexelsapp.domain.manager

import android.content.Intent
import androidx.activity.result.IntentSenderRequest
import com.sdv.kit.pexelsapp.domain.model.UserDetails

interface GoogleAuthManager {
    suspend fun signIn(intent: Intent? = null): UserDetails?
    suspend fun buildIntentSenderRequest(): IntentSenderRequest?
    suspend fun signOut()
    fun getSignedInUser(): UserDetails?
}