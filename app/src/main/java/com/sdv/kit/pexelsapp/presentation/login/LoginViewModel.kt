package com.sdv.kit.pexelsapp.presentation.login

import android.content.Intent
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdv.kit.pexelsapp.domain.manager.GoogleAuthManager
import com.sdv.kit.pexelsapp.domain.model.UserDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val googleAuthManager: GoogleAuthManager
) : ViewModel() {

    private val authResultExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        _errorState.value = LoginException.SignInError
    }

    private val intentSenderRequestExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        _errorState.value = LoginException.LaunchError
    }

    private val _errorState = mutableStateOf<LoginException?>(null)
    val errorState: State<LoginException?> = _errorState

    private val _authResult = mutableStateOf<UserDetails?>(null)
    val authResult: State<UserDetails?> = _authResult

    private val _intentSenderRequest = mutableStateOf<IntentSenderRequest?>(null)
    val intentSenderRequest: State<IntentSenderRequest?> = _intentSenderRequest

    fun buildIntentSenderRequest() {
        viewModelScope.launch(Dispatchers.IO + intentSenderRequestExceptionHandler) {
            _intentSenderRequest.value = googleAuthManager.buildIntentSenderRequest()
        }
    }

    fun signIn(data: Intent?) {
        viewModelScope.launch(Dispatchers.IO + authResultExceptionHandler) {
            _authResult.value = googleAuthManager.signIn(intent = data)
        }
    }

    fun hideError() {
        _errorState.value = null
    }

    sealed interface LoginException {
        data object LaunchError : LoginException
        data object SignInError : LoginException
    }
}