package com.sdv.kit.pexelsapp.presentation.common.stub

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme

@Composable
fun ImageNotFoundStub(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.image_not_found_stub),
            color = AppTheme.colors.textColorVariant,
            style = AppTheme.typography.labelMedium,
            textAlign = TextAlign.Center
        )
        TextButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = onClick
        ) {
            Text(
                text = stringResource(R.string.explore),
                color = AppTheme.colors.primary,
                style = AppTheme.typography.labelLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@LightAndDarkPreview
@Composable
fun ImageNotFoundStubPreview() {
    AppTheme {
        Surface(
            modifier = Modifier.background(
                color = AppTheme.colors.surface
            )
        ) {
            ImageNotFoundStub(
                modifier = Modifier.padding(all = Dimens.PADDING_MEDIUM),
                onClick = { }
            )
        }
    }
}