package com.sdv.kit.pexelsapp.data.manager

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sdv.kit.pexelsapp.domain.manager.GoogleAuthManager
import com.sdv.kit.pexelsapp.domain.model.UserDetails
import com.sdv.kit.pexelsapp.util.Constants.GOOGLE_WEB_CLIENT_ID
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GoogleAuthManagerImpl @Inject constructor(context: Context) : GoogleAuthManager {

    private val oneTapClient = Identity.getSignInClient(context)
    private val auth = Firebase.auth

    override suspend fun signIn(intent: Intent?): UserDetails {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        val user = auth.signInWithCredential(googleCredentials).await().user ?: throw RuntimeException("Error")

        return with(user) {
            UserDetails(
                userId = uid,
                username = displayName,
                profilePictureUrl = photoUrl?.toString(),
                phoneNumber = phoneNumber,
                email = email ?: ""
            )
        }
    }

    override suspend fun buildIntentSenderRequest(): IntentSenderRequest? {
        return IntentSenderRequest
            .Builder(buildIntentSender() ?: return null)
            .build()
    }

    override suspend fun signOut() {
        oneTapClient.signOut().await()
        auth.signOut()
    }

    override fun getSignedInUser(): UserDetails? {
        return auth.currentUser?.run {
            UserDetails(
                userId = uid,
                username = displayName,
                profilePictureUrl = photoUrl?.toString(),
                phoneNumber = phoneNumber,
                email = email ?: ""
            )
        }
    }

    private suspend fun buildIntentSender(): IntentSender? {
        val result = oneTapClient.beginSignIn(buildSignInRequest()).await()
        return result?.pendingIntent?.intentSender
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(GOOGLE_WEB_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()
    }
}