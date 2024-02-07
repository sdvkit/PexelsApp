package com.sdv.kit.pexelsapp.presentation.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme
import com.sdv.kit.pexelsapp.presentation.ui.theme.Grey86

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    colors: TextFieldColors = TextFieldDefaults.colors(
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
    value: TextFieldValue,
    onSearch: (TextFieldValue) -> Unit,
    onValueChange: (TextFieldValue) -> Unit,
    onClear: () -> Unit
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        colors = colors,
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(value)
            }
        ),
        leadingIcon = {
            Icon(
                modifier = Modifier.size(Dimens.SEARCH_BAR_SEARCH_ICON_SIZE),
                painter = painterResource(R.drawable.ic_search),
                contentDescription = null,
                tint = AppTheme.colors.primary
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = value.text.isNotEmpty(),
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                IconButton(
                    modifier = Modifier.size(Dimens.SEARCH_BAR_CLEAR_ICON_SIZE),
                    onClick = onClear
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
                text = stringResource(R.string.search),
                style = AppTheme.typography.bodyMedium
            )
        }
    )
}

@LightAndDarkPreview
@Composable
fun SearchBarPreview() {
    AppTheme {
        val searchValue by remember {
            mutableStateOf(TextFieldValue(""))
        }

        Surface(
            modifier = Modifier.background(
                color = AppTheme.colors.surface
            )
        ) {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = Dimens.PADDING_MEDIUM),
                value = searchValue,
                onValueChange = { },
                onSearch = { },
                onClear = { }
            )
        }
    }
}