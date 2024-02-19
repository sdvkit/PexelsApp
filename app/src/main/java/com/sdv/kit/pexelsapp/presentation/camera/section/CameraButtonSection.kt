package com.sdv.kit.pexelsapp.presentation.camera.section

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.anim.bounceClickEffect
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme
import com.sdv.kit.pexelsapp.presentation.ui.theme.Black
import com.sdv.kit.pexelsapp.presentation.ui.theme.White

@Composable
fun CameraButtonSection(
    modifier: Modifier = Modifier,
    onCaptureButtonClicked: () -> Unit,
    onRotateButtonClicked: () -> Unit,
    onFlashlightButtonClicked: () -> Unit
) {
    var isFlashlightActive by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(1f))
        RotateButton(
            modifier = Modifier
                .size(Dimens.DEFAULT_BOTTOM_CAMERA_BUTTON_SIZE)
                .bounceClickEffect(),
            onClick = onRotateButtonClicked,
            icon = R.drawable.ic_photo_rotate,
            iconTint = Black
        )
        Spacer(modifier = Modifier.width(Dimens.PADDING_SMALL))
        CaptureButton(
            modifier = Modifier.size(Dimens.CAPTURE_BUTTON_SIZE),
            onClick = onCaptureButtonClicked
        )
        Spacer(modifier = Modifier.width(Dimens.PADDING_SMALL))
        FlashlightButton(
            modifier = Modifier
                .size(Dimens.DEFAULT_BOTTOM_CAMERA_BUTTON_SIZE)
                .bounceClickEffect(),
            onClick = {
                isFlashlightActive = !isFlashlightActive
                onFlashlightButtonClicked()
            },
            icon = R.drawable.ic_flashlight,
            iconTint = Black,
            isFlashlightActive
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun CaptureButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier.border(
            border = BorderStroke(
                width = 4.dp,
                color = White
            ),
            shape = CircleShape
        ),
        contentAlignment = Alignment.Center
    ) {
        Button(
            modifier = Modifier
                .fillMaxSize(0.8f)
                .bounceClickEffect(0.8f),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = White
            ),
            onClick = { onClick() },
            content = { }
        )
    }
}

@Composable
private fun RotateButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @DrawableRes icon: Int,
    iconTint: Color
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = White
        )
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = iconTint
        )
    }
}

@Composable
private fun FlashlightButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @DrawableRes icon: Int,
    iconTint: Color,
    isActive: Boolean
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) Color.Yellow else White
        )
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = iconTint
        )
    }
}

@LightAndDarkPreview
@Composable
fun CameraButtonSectionPreview() {
    AppTheme {
        CameraButtonSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = Dimens.PADDING_MEDIUM),
            onCaptureButtonClicked = { },
            onRotateButtonClicked = { },
            onFlashlightButtonClicked = { }
        )
    }
}