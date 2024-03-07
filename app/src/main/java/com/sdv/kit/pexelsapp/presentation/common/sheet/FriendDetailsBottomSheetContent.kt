package com.sdv.kit.pexelsapp.presentation.common.sheet

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.anim.bounceClickEffect
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme

@Composable
fun FriendDetailsBottomSheetContent(
    modifier: Modifier = Modifier,
    onSendButtonClicked: () -> Unit,
    onDeleteButtonClicked: () -> Unit
) {
    Column(modifier = modifier) {
        DetailsAction(
            modifier = Modifier
                .fillMaxWidth()
                .bounceClickEffect(),
            onClick = onSendButtonClicked,
            icon = R.drawable.ic_send,
            tint = AppTheme.colors.textColor,
            text = R.string.send_message
        )
        DetailsAction(
            modifier = Modifier
                .fillMaxWidth()
                .bounceClickEffect(),
            onClick = onDeleteButtonClicked,
            icon = R.drawable.ic_delete,
            tint = AppTheme.colors.primary,
            text = R.string.delete_friend
        )
    }
}

@Composable
private fun DetailsAction(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    tint: Color,
    @StringRes text: Int,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(Dimens.DETAILS_DELETE_BUTTON_ICON_SIZE),
            painter = painterResource(icon),
            contentDescription = null,
            tint = tint
        )
        Spacer(modifier = Modifier.width(Dimens.PADDING_SMALL))
        Text(
            text = stringResource(text),
            color = tint,
            style = AppTheme.typography.labelLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@LightAndDarkPreview
@Composable
fun FriendDetailsBottomSheetContentPreview() {
    AppTheme {
        FriendDetailsBottomSheetContent(
            modifier = Modifier.fillMaxWidth(),
            onDeleteButtonClicked = { },
            onSendButtonClicked = { }
        )
    }
}