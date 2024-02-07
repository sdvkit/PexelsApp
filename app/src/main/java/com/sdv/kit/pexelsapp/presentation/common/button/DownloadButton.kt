package com.sdv.kit.pexelsapp.presentation.common.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme
import com.sdv.kit.pexelsapp.presentation.ui.theme.White

@Composable
fun DownloadButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    downloadIconSize: Dp = Dimens.DOWNLOAD_BUTTON_ICON_SIZE
) {
    FilledIconButton(
        modifier = modifier,
        shape = CircleShape,
        onClick = onClick,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = AppTheme.colors.surfaceVariant
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilledIconButton(
                modifier = Modifier.size(downloadIconSize),
                onClick = onClick,
                shape = CircleShape,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = AppTheme.colors.primary
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_download),
                    contentDescription = null,
                    tint = White
                )
            }
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.download),
                style = AppTheme.typography.labelMedium,
                color = AppTheme.colors.textColorVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(Dimens.PADDING_SMALL))
        }
    }
}

@LightAndDarkPreview
@Composable
fun DownloadButtonPreview() {
    AppTheme {
        Surface(
            modifier = Modifier.background(
                color = AppTheme.colors.surface
            )
        ) {
            DownloadButton(
                modifier = Modifier
                    .width(Dimens.DOWNLOAD_BUTTON_WIDTH)
                    .height(Dimens.DOWNLOAD_BUTTON_HEIGHT),
                onClick = { }
            )
        }
    }
}