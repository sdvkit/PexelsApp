package com.sdv.kit.pexelsapp.presentation.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun TextSpacer(
    modifier: Modifier = Modifier,
    style: TextStyle
) {
    Text(
        modifier = modifier,
        text = " ",
        style = style
    )
}