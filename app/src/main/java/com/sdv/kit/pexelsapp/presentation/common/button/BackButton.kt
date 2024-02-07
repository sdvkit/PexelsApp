package com.sdv.kit.pexelsapp.presentation.common.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme

@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    iconSize: Dp = Dimens.BACK_BUTTON_ICON_SIZE,
    onClick: () -> Unit
) {
    FilledIconButton(
        modifier = modifier,
        onClick = onClick,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = AppTheme.colors.surfaceVariant
        ),
        shape = AppTheme.shapes.small
    ) {
        Icon(
            modifier = Modifier.size(iconSize),
            painter = painterResource(R.drawable.ic_back),
            contentDescription = null,
            tint = AppTheme.colors.textColor
        )
    }
}

@LightAndDarkPreview
@Composable
fun BackButtonPreview() {
    AppTheme {
        Surface(
            modifier = Modifier.background(
                color = AppTheme.colors.surface
            )
        ) {
            BackButton(
                modifier = Modifier.size(Dimens.BACK_BUTTON_SIZE),
                onClick = {}
            )
        }
    }
}