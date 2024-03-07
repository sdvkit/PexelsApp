package com.sdv.kit.pexelsapp.presentation.common.stub

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.common.AnimatedLottieImage
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme

@Composable
fun NoSearchResultsStub(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedLottieImage(
            modifier = Modifier.size(Dimens.STUB_ANIMATED_IMAGE_SIZE),
            res = R.raw.image_no_search_results_stub_anim
        )
        Spacer(modifier = Modifier.height(Dimens.PADDING_SMALL))
        Text(
            text = stringResource(R.string.no_results_stub),
            color = AppTheme.colors.textColorVariant,
            style = AppTheme.typography.labelMedium,
            textAlign = TextAlign.Center
        )
    }
}

@LightAndDarkPreview
@Composable
fun NoSearchResultsStubPreview() {
    AppTheme {
        Surface(
            modifier = Modifier.background(
                color = AppTheme.colors.surface
            )
        ) {
            NoSearchResultsStub(
                modifier = Modifier.padding(all = Dimens.PADDING_MEDIUM)
            )
        }
    }
}