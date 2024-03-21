package com.sdv.kit.pexelsapp.presentation.common.stub

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.sdv.kit.pexelsapp.presentation.common.AnimatedLottieImage
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme

@Composable
fun NoChatsStub(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        AnimatedLottieImage(
            modifier = Modifier.size(Dimens.STUB_ANIMATED_IMAGE_SIZE),
            res = R.raw.image_no_chats_stub_anim
        )
        Spacer(modifier = Modifier.height(Dimens.PADDING_SMALL))
        Text(
            text = stringResource(R.string.no_chats_stub),
            color = AppTheme.colors.textColorVariant,
            style = AppTheme.typography.labelMedium,
            textAlign = TextAlign.Center
        )
        TextButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = onClick
        ) {
            Text(
                text = stringResource(R.string.new_chat),
                color = AppTheme.colors.primary,
                style = AppTheme.typography.labelLarge,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@LightAndDarkPreview
@Composable
fun NoChatsStubPreview() {
    AppTheme {
        Surface(
            modifier = Modifier.background(
                color = AppTheme.colors.surface
            )
        ) {
            NoChatsStub(
                modifier = Modifier.padding(all = Dimens.PADDING_MEDIUM),
                onClick = { }
            )
        }
    }
}