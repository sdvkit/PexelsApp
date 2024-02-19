package com.sdv.kit.pexelsapp.presentation.camera

import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.anim.bounceClickEffect
import com.sdv.kit.pexelsapp.presentation.camera.section.CameraButtonSection
import com.sdv.kit.pexelsapp.presentation.common.button.BackButton

@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    controller: LifecycleCameraController,
    onBackButtonClicked: () -> Unit,
    onTakePhoto: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    var isTorchEnabled by remember {
        mutableStateOf(false)
    }

    Box(modifier = modifier) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                PreviewView(it).apply {
                    this.controller = controller
                    controller.bindToLifecycle(lifecycleOwner)
                }
            }
        )
        BackButton(
            modifier = Modifier
                .offset(
                    x = Dimens.PADDING_MEDIUM_SMALLER,
                    y = Dimens.PADDING_MEDIUM_SMALLER
                )
                .size(Dimens.BACK_BUTTON_SIZE)
                .bounceClickEffect(),
            onClick = onBackButtonClicked
        )
        CameraButtonSection(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(all = Dimens.PADDING_MEDIUM),
            onCaptureButtonClicked = {
                onTakePhoto()
            },
            onFlashlightButtonClicked = {
                if (controller.cameraInfo?.hasFlashUnit() == true) {
                    isTorchEnabled = !isTorchEnabled
                    controller.enableTorch(isTorchEnabled)
                }
            },
            onRotateButtonClicked = {
                controller.cameraSelector =
                    if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    } else CameraSelector.DEFAULT_BACK_CAMERA
            }
        )
    }
}