package com.sdv.kit.pexelsapp.presentation.common.sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.anim.bounceClickEffect
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme

@Composable
fun ConversationDetailsBottomSheetContent(
    modifier: Modifier = Modifier,
    onDeleteButtonClicked: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .bounceClickEffect(),
            onClick = onDeleteButtonClicked
        ) {
            Icon(
                modifier = Modifier.size(Dimens.DETAILS_DELETE_BUTTON_ICON_SIZE),
                painter = painterResource(R.drawable.ic_delete),
                contentDescription = null,
                tint = AppTheme.colors.primary
            )
            Spacer(modifier = Modifier.width(Dimens.PADDING_SMALL))
            Text(
                text = stringResource(R.string.delete_chat),
                color = AppTheme.colors.primary,
                style = AppTheme.typography.labelLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@LightAndDarkPreview
@Composable
fun ConversationDetailsBottomSheetContentPreview() {
    AppTheme {
        ConversationDetailsBottomSheetContent(
            modifier = Modifier.fillMaxWidth(),
            onDeleteButtonClicked = { }
        )
    }
}