package com.sdv.kit.pexelsapp.presentation.profile.section

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.anim.bounceClickEffect
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.common.NonLazyGrid
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme

@Composable
fun LocalPhotosSection(
    modifier: Modifier = Modifier,
    isStoragePermissionGranted: Boolean,
    onPermissionRequest: () -> Unit,
    onTakePhotoButtonClicked: () -> Unit,
    images: List<Bitmap>
) {
    Column(modifier = modifier) {
        SectionHeader(onTakePhotoButtonClicked = onTakePhotoButtonClicked)
        Spacer(modifier = Modifier.height(Dimens.PADDING_MEDIUM))
        SectionBody(
            modifier = Modifier.fillMaxWidth(),
            images = images,
            isStoragePermissionGranted = isStoragePermissionGranted,
            onPermissionRequest = onPermissionRequest
        )
    }
}

@Composable
private fun SectionBody(
    modifier: Modifier = Modifier,
    isStoragePermissionGranted: Boolean,
    onPermissionRequest: () -> Unit,
    images: List<Bitmap>
) {
    when {
        isStoragePermissionGranted && images.isNotEmpty() -> {
            NonLazyGrid(
                modifier = modifier,
                columns = 2,
                itemCount = images.size,
                spaceBetween = Dimens.PADDING_MEDIUM_SMALLER
            ) { index ->
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bounceClickEffect(valueTo = 0.9f)
                        .clip(AppTheme.shapes.medium),
                    bitmap = images[index].asImageBitmap(),
                    contentDescription = null
                )
            }
        }

        isStoragePermissionGranted -> {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.no_local_photos),
                    color = AppTheme.colors.primary,
                    style = AppTheme.typography.displayMedium
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        else -> {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = onPermissionRequest) {
                    Text(
                        text = stringResource(R.string.allow_permission),
                        color = AppTheme.colors.primary,
                        style = AppTheme.typography.displayMedium
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun SectionHeader(
    modifier: Modifier = Modifier,
    onTakePhotoButtonClicked: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.local_photos),
            color = AppTheme.colors.textColor,
            style = AppTheme.typography.displayMedium
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier
                .size(Dimens.TAKE_PHOTO_BUTTON_SIZE)
                .bounceClickEffect(0.9f),
            onClick = onTakePhotoButtonClicked,
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppTheme.colors.primary
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_camera),
                contentDescription = null
            )
        }
    }
}

@LightAndDarkPreview
@Composable
fun LocalPhotosSectionPreview() {
    AppTheme {
        LocalPhotosSection(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = AppTheme.colors.surface),
            onTakePhotoButtonClicked = { },
            images = listOf(),
            isStoragePermissionGranted = false,
            onPermissionRequest = { }
        )
    }
}