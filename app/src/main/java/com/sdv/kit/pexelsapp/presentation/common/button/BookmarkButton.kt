package com.sdv.kit.pexelsapp.presentation.common.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme
import com.sdv.kit.pexelsapp.presentation.ui.theme.White

@Composable
fun BookmarkButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isActive: Boolean
) {
    val textColor = if (isActive) White else AppTheme.colors.textColorVariant
    val backgroundColor = if (isActive) AppTheme.colors.primary else AppTheme.colors.surfaceVariant

    FilledIconButton(
        modifier = modifier,
        onClick = onClick,
        shape = CircleShape,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = backgroundColor
        )
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_bookmark_inactive),
            contentDescription = null,
            tint = textColor
        )
    }
}

@LightAndDarkPreview
@Composable
fun BookmarkButtonInactivePreview() {
    AppTheme {
        Surface(
            modifier = Modifier.background(
                color = AppTheme.colors.surface
            )
        ) {
            BookmarkButton(
                modifier = Modifier.size(Dimens.BOOKMARK_BUTTON_SIZE),
                onClick = { },
                isActive = false
            )
        }
    }
}

@LightAndDarkPreview
@Composable
fun BookmarkButtonActivePreview() {
    AppTheme {
        Surface(
            modifier = Modifier.background(
                color = AppTheme.colors.surface
            )
        ) {
            BookmarkButton(
                modifier = Modifier.size(Dimens.BOOKMARK_BUTTON_SIZE),
                onClick = { },
                isActive = true
            )
        }
    }
}