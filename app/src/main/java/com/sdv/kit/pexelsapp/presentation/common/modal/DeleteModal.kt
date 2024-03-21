package com.sdv.kit.pexelsapp.presentation.common.modal

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme

@Composable
fun DeleteModal(
    @DrawableRes icon: Int? = null,
    @StringRes title: Int,
    @StringRes text: Int,
    @StringRes confirmLabel: Int? = null,
    @StringRes dismissLabel: Int? = null,
    onDismissRequest: () -> Unit = { },
    onConfirmation: () -> Unit = { },
) {
    AlertDialog(
        icon = {
            if (icon != null) {
                Icon(
                    painter = painterResource(icon),
                    tint = AppTheme.colors.primary,
                    contentDescription = null
                )
            }
        },
        title = {
            Text(
                text = stringResource(title),
                style = AppTheme.typography.displayMedium,
                color = AppTheme.colors.textColor
            )
        },
        text = {
            Text(
                text = stringResource(text),
                style = AppTheme.typography.bodySmall,
                color = AppTheme.colors.textColor
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            if (confirmLabel != null) {
                TextButton(
                    onClick = {
                        onConfirmation()
                    }
                ) {
                    Text(
                        text = stringResource(confirmLabel),
                        style = AppTheme.typography.labelMedium,
                        color = AppTheme.colors.primary
                    )
                }
            }
        },
        dismissButton = {
            if (dismissLabel != null) {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        style = AppTheme.typography.labelMedium,
                        color = AppTheme.colors.primary
                    )
                }
            }
        }
    )
}