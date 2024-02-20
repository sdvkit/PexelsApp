package com.sdv.kit.pexelsapp.presentation.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdv.kit.pexelsapp.domain.manager.GoogleAuthManager
import com.sdv.kit.pexelsapp.domain.usecase.permission.GetPermissionEntry
import com.sdv.kit.pexelsapp.domain.usecase.permission.SavePermissionEntry
import com.sdv.kit.pexelsapp.domain.usecase.photo.GetLocalImagesByPrefix
import com.sdv.kit.pexelsapp.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val googleAuthManager: GoogleAuthManager,
    private val getLocalImagesByPrefixUsecase: GetLocalImagesByPrefix,
    private val getPermissionEntryUsecase: GetPermissionEntry,
    private val savePermissionEntryUsecase: SavePermissionEntry
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

    fun getStoragePermissionEntry(permission: String) {
        viewModelScope.launch(Dispatchers.Main) {
            val permissionEntry = getPermissionEntryUsecase(permission = permission)

            _profileState.value = _profileState.value.copy(
                lastStoragePermissionEntry = permission to permissionEntry
            )
        }
    }

    fun saveStoragePermissionEntry(permission: String) {
        viewModelScope.launch(Dispatchers.IO) {
            savePermissionEntryUsecase(permission = permission)
            _profileState.value = _profileState.value.copy(
                lastStoragePermissionEntry = permission to true
            )
        }
    }

    fun getCameraPermissionEntry(permission: String) {
        viewModelScope.launch(Dispatchers.Main) {
            val permissionEntry = getPermissionEntryUsecase(permission = permission)

            _profileState.value = _profileState.value.copy(
                lastCameraPermissionEntry = permission to permissionEntry
            )
        }
    }

    fun saveCameraPermissionEntry(permission: String) {
        viewModelScope.launch(Dispatchers.IO) {
            savePermissionEntryUsecase(permission = permission)
            _profileState.value = _profileState.value.copy(
                lastCameraPermissionEntry = permission to true
            )
        }
    }

    fun getLocalImages() {
        viewModelScope.launch(Dispatchers.IO) {
            val localImages = getLocalImagesByPrefixUsecase(prefix = Constants.IMAGE_NAME_PREFIX)

            withContext(Dispatchers.Main) {
                _profileState.value = _profileState.value.copy(
                    localImages = localImages
                )
            }
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