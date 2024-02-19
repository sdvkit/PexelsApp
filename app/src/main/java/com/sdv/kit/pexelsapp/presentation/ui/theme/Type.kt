package com.sdv.kit.pexelsapp.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sdv.kit.pexelsapp.R

val mulishFontFamily = FontFamily(
    Font(
        resId = R.font.mulish_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.mulish_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resId = R.font.mulish_bold,
        weight = FontWeight.Bold
    )
)

val Typography = Typography(
    bodySmall = TextStyle(
        fontFamily = mulishFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = mulishFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = mulishFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp
    ),
    labelSmall = TextStyle(
        fontFamily = mulishFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    ),
    labelMedium = TextStyle(
        fontFamily = mulishFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    labelLarge = TextStyle(
        fontFamily = mulishFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    titleMedium = TextStyle(
        fontFamily = mulishFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    displaySmall = TextStyle(
        fontFamily = mulishFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    displayMedium = TextStyle(
        fontFamily = mulishFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp
    ),
    displayLarge = TextStyle(
        fontFamily = mulishFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    )
)