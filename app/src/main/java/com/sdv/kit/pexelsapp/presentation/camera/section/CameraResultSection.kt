package com.sdv.kit.pexelsapp.presentation.camera.section

import android.graphics.Bitmap
import android.graphics.Picture
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.anim.bounceClickEffect
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme
import com.sdv.kit.pexelsapp.presentation.ui.theme.White

@Composable
fun CameraResultSection(
    modifier: Modifier = Modifier,
    bitmap: Bitmap,
    onSaveButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .background(color = AppTheme.colors.surface),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = Modifier.weight(1f))
        BottomOptions(
            onSaveButtonClicked = onSaveButtonClicked,
            onCancelButtonClicked = onCancelButtonClicked
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun BottomOptions(
    modifier: Modifier = Modifier,
    onSaveButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit
) {
    Row(modifier = modifier) {
        Button(
            modifier = Modifier.bounceClickEffect(0.9f),
            onClick = onCancelButtonClicked,
            colors = ButtonDefaults.buttonColors(
                containerColor = AppTheme.colors.surfaceVariant
            )
        ) {
            Text(
                text = stringResource(R.string.cancel),
                style = AppTheme.typography.labelMedium,
                color = AppTheme.colors.textColor
            )
        }
        Spacer(modifier = Modifier.width(Dimens.PADDING_SMALL))
        Button(
            modifier = Modifier.bounceClickEffect(0.9f),
            onClick = onSaveButtonClicked,
            colors = ButtonDefaults.buttonColors(
                containerColor = AppTheme.colors.primary
            )
        ) {
            Text(
                text = stringResource(R.string.save),
                style = AppTheme.typography.labelMedium,
                color = White
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@LightAndDarkPreview
@Composable
fun CameraResultSectionPreview() {
    AppTheme {
        CameraResultSection(
            modifier = Modifier.fillMaxSize(),
            bitmap = Bitmap.createBitmap(Picture()),
            onSaveButtonClicked = { },
            onCancelButtonClicked = { }
        )
    }
}