package com.sdv.kit.pexelsapp.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme
import com.sdv.kit.pexelsapp.presentation.ui.theme.White

@Composable
fun FeaturedCollectionHeader(
    modifier: Modifier = Modifier,
    text: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val containerColor = if (isActive) AppTheme.colors.primary else AppTheme.colors.surfaceVariant
    val textColor = if (isActive) White else AppTheme.colors.textColor

    Button(
        modifier = modifier.heightIn(
            min = Dimens.FEATURED_COLLECTION_HEADER_HEIGHT
        ),
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor
        )
    ) {
        Text(
            text = text,
            style = AppTheme.typography.bodyMedium,
            color = textColor
        )
    }
}

@LightAndDarkPreview
@Composable
fun FeaturedCollectionHeaderPreview() {
    AppTheme {
        Surface(modifier = Modifier.background(color = AppTheme.colors.surface)) {
            FeaturedCollectionHeader(
                modifier = Modifier.padding(all = Dimens.PADDING_MEDIUM),
                text = "Watches",
                isActive = false,
                onClick = { }
            )
        }
    }
}

@LightAndDarkPreview
@Composable
fun FeaturedCollectionHeaderActivePreview() {
    AppTheme {
        Surface(modifier = Modifier.background(color = AppTheme.colors.surface)) {
            FeaturedCollectionHeader(
                modifier = Modifier.padding(all = Dimens.PADDING_MEDIUM),
                text = "Watches",
                isActive = true,
                onClick = { }
            )
        }
    }
}
