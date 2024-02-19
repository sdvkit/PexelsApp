package com.sdv.kit.pexelsapp.presentation.camera

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdv.kit.pexelsapp.domain.usecase.permission.GetPermissionEntry
import com.sdv.kit.pexelsapp.domain.usecase.permission.SavePermissionEntry
import com.sdv.kit.pexelsapp.domain.usecase.photo.SavePhotoImageToGallery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val savePhotoImageToGalleryUsecase: SavePhotoImageToGallery,
    private val getPermissionEntryUsecase: GetPermissionEntry,
    private val savePermissionEntryUsecase: SavePermissionEntry
) : ViewModel() {

    private val _state = mutableStateOf(CameraState())
    val state: State<CameraState> = _state

    private val photoExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()

        _state.value = _state.value.copy(
            lastBitmap = null,
            errorMessage = throwable.message
        )
    }

    fun getStoragePermissionEntry(permission: String) {
        viewModelScope.launch {
            val permissionEntry = getPermissionEntryUsecase(permission = permission)

            _state.value = _state.value.copy(
                lastPermissionEntry = permission to permissionEntry
            )
        }
    }

    fun savePermissionEntry(permission: String) {
        viewModelScope.launch {
            savePermissionEntryUsecase(permission = permission)
            _state.value = _state.value.copy(
                lastPermissionEntry = permission to true
            )
        }
    }

    fun savePhoto() {
        viewModelScope.launch(Dispatchers.IO + photoExceptionHandler) {
            savePhotoImageToGalleryUsecase(
                bitmap = _state.value.lastBitmap ?: throw RuntimeException("Can't save photo")
            )

            _state.value = _state.value.copy(
                lastBitmap = null,
                errorMessage = null
            )
        }
    }

    fun cancelSave() {
        _state.value = _state.value.copy(
            lastBitmap = null,
            errorMessage = null
        )
    }

    fun takePhoto(controller: LifecycleCameraController) {
        controller.takePicture(
            Dispatchers.IO.asExecutor(),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    val matrix = Matrix().apply {
                        postRotate(image.imageInfo.rotationDegrees.toFloat())
                    }
                    val rotatedBitmap = Bitmap.createBitmap(
                        image.toBitmap(),
                        0,
                        0,
                        image.width,
                        image.height,
                        matrix,
                        true
                    )

                    _state.value = _state.value.copy(
                        lastBitmap = rotatedBitmap,
                        errorMessage = null
                    )
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)

                    _state.value = _state.value.copy(
                        lastBitmap = null,
                        errorMessage = exception.message
                    )
                }
            }
        )
    }
}