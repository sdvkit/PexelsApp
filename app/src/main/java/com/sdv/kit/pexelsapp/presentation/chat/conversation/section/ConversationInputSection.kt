package com.sdv.kit.pexelsapp.presentation.chat.conversation.section

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.anim.bounceClickEffect
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme
import com.sdv.kit.pexelsapp.presentation.ui.theme.Grey86
import com.sdv.kit.pexelsapp.presentation.ui.theme.White

@Composable
fun ConversationInputSection(
    modifier: Modifier = Modifier,
    onAttachButtonClicked: () -> Unit,
    onSendButtonClicked: (String) -> Unit
) {
    val message = remember { mutableStateOf("") }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        InputButton(
            modifier = Modifier
                .size(Dimens.CONVERSATION_INPUT_BUTTON_SIZE)
                .bounceClickEffect(),
            icon = R.drawable.ic_plus,
            iconTint = AppTheme.colors.primary,
            containerColor = AppTheme.colors.surfaceVariant,
            onClick = onAttachButtonClicked
        )
        Spacer(modifier = Modifier.width(Dimens.PADDING_EXTRA_SMALL))
        MessageField(
            modifier = Modifier
                .weight(1f),
            message = message
        )
        Spacer(modifier = Modifier.width(Dimens.PADDING_EXTRA_SMALL))
        AnimatedVisibility(visible = message.value.isNotBlank()) {
            InputButton(
                modifier = Modifier
                    .size(Dimens.CONVERSATION_INPUT_BUTTON_SIZE)
                    .bounceClickEffect(),
                icon = R.drawable.ic_send,
                iconTint = White,
                containerColor = AppTheme.colors.primary,
                onClick = {
                    onSendButtonClicked(message.value)
                    message.value = ""
                }
            )
        }
    }
}

@Composable
private fun MessageField(
    modifier: Modifier = Modifier,
    message: MutableState<String>
) {
    TextField(
        modifier = modifier,
        value = message.value,
        onValueChange = { newValue ->
            message.value = newValue
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedContainerColor = AppTheme.colors.surfaceVariant,
            focusedContainerColor = AppTheme.colors.surfaceVariant,
            focusedTextColor = AppTheme.colors.textColor,
            unfocusedTextColor = AppTheme.colors.textColor,
            unfocusedPlaceholderColor = AppTheme.colors.secondaryTextColor,
            focusedPlaceholderColor = AppTheme.colors.secondaryTextColor,
            cursorColor = AppTheme.colors.primary,
            selectionColors = TextSelectionColors(
                handleColor = AppTheme.colors.primary,
                backgroundColor = AppTheme.colors.secondaryTextColor
            )
        ),
        singleLine = true,
        trailingIcon = {
            AnimatedVisibility(
                visible = message.value.isNotEmpty(),
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                IconButton(
                    modifier = Modifier.size(Dimens.CONVERSATION_MESSAGE_CLEAR_ICON_SIZE),
                    onClick = {
                        message.value = ""
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_cross),
                        contentDescription = null,
                        tint = Grey86
                    )
                }
            }
        },
        textStyle = AppTheme.typography.bodyMedium,
        shape = CircleShape,
        placeholder = {
            Text(
                text = stringResource(R.string.enter_message),
                style = AppTheme.typography.bodyMedium
            )
        }
    )
}

@Composable
private fun InputButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    iconTint: Color,
    containerColor: Color,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor
        )
    ) {
        Icon(
            painter = painterResource(icon),
            tint = iconTint,
            contentDescription = null
        )
    }
}

@LightAndDarkPreview
@Composable
fun ConversationInputSectionPreview() {
    AppTheme {
        ConversationInputSection(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = AppTheme.colors.surface),
            onAttachButtonClicked = { },
            onSendButtonClicked = { }
        )
    }
}