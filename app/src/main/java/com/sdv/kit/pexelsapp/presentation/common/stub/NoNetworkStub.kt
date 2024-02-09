package com.sdv.kit.pexelsapp.presentation.common.stub

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme

@Composable
fun NoNetworkStub(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(modifier = modifier) {
        Icon(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            painter = painterResource(R.drawable.ic_no_network),
            contentDescription = null,
            tint = AppTheme.colors.stubIconTint
        )
        TextButton(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            onClick = onClick
        ) {
            Text(
                text = stringResource(R.string.try_again),
                color = AppTheme.colors.primary,
                style = AppTheme.typography.labelLarge
            )
        }
    }
}

@LightAndDarkPreview
@Composable
fun NoNetworkStubPreview() {
    AppTheme {
        Surface {
            NoNetworkStub(
                modifier = Modifier
                    .padding(all = Dimens.PADDING_MEDIUM)
                    .height(Dimens.NO_NETWORK_STUB_HEIGHT)
                    .width(Dimens.NO_NETWORK_STUB_WIDTH),
                onClick = { }
            )
        }
    }
}