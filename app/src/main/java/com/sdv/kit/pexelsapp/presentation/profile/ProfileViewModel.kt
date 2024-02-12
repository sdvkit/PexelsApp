package com.sdv.kit.pexelsapp.presentation.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdv.kit.pexelsapp.domain.manager.GoogleAuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val googleAuthManager: GoogleAuthManager
) : ViewModel() {

    private val _profileState = mutableStateOf(ProfileState())
    val profileState: State<ProfileState> = _profileState

    private val userDetailsExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    init {
        viewModelScope.launch(userDetailsExceptionHandler) {
            _profileState.value = _profileState.value.copy(
                userDetails = googleAuthManager.getSignedInUser()!!,
                isSignedOut = false
            )
        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO + userDetailsExceptionHandler) {
            googleAuthManager.signOut()
            _profileState.value = _profileState.value.copy(
                userDetails = null,
                isSignedOut = true
            )
        }
    }
}