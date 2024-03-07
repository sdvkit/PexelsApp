package com.sdv.kit.pexelsapp.presentation.common.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme

@Composable
fun DetailsButton(
    modifier: Modifier = Modifier,
    iconSize: Dp = Dimens.DETAILS_BUTTON_ICON_SIZE,
    iconTint: Color = AppTheme.colors.textColor,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(iconSize),
            painter = painterResource(R.drawable.ic_details),
            contentDescription = null,
            tint = iconTint
        )
    }
}

@LightAndDarkPreview
@Composable
fun DetailsButtonPreview() {
    AppTheme {
        Surface(
            modifier = Modifier.background(
                color = AppTheme.colors.surface
            )
        ) {
            DetailsButton(
                modifier = Modifier.size(Dimens.DETAILS_BUTTON_SIZE),
                onClick = {}
            )
        }
    }
}