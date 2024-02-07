package com.sdv.kit.pexelsapp.presentation.common.card

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.anim.shimmerEffect
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme

@Composable
fun PhotoCardShimmer(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.shimmerEffect(shape = AppTheme.shapes.medium)
    )
}

@LightAndDarkPreview
@Composable
fun PhotoCardShimmerPreview() {
    AppTheme {
        Surface {
            PhotoCardShimmer()
        }
    }
}