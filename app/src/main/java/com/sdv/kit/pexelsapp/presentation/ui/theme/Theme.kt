package com.sdv.kit.pexelsapp.presentation.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = AppColorScheme(
    primary = Red,
    surface = White,
    surfaceVariant = WhiteVariant,
    textColor = Black,
    secondaryTextColor = Grey86,
    progressBackground = WhiteVariant,
    textColorVariant = Grey33,
    stubIconTint = Color.Black
)

private val DarkColorScheme = AppColorScheme(
    primary = Red,
    surface = Black,
    surfaceVariant = Grey39,
    textColor = White,
    secondaryTextColor = GreyVariant,
    progressBackground = GreyVariant,
    textColorVariant = White,
    stubIconTint = Color.White
)

@Composable
fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (isDarkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.surface.toArgb()
            val controller = WindowCompat.getInsetsController(window, view)
            controller.isAppearanceLightStatusBars = !isDarkTheme
        }
    }

    CompositionLocalProvider(
        LocalColorPalette provides colors,
        LocalTypography provides Typography,
        LocalShapes provides Shapes,
        content = content
    )
}

data class AppColorScheme(
    val primary: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val textColor: Color,
    val secondaryTextColor: Color,
    val textColorVariant: Color,
    val progressBackground: Color,
    val stubIconTint: Color
)

object AppTheme {
    val colors: AppColorScheme @Composable get() = LocalColorPalette.current
    val typography: Typography @Composable get() = LocalTypography.current
    val shapes: Shapes @Composable get() = LocalShapes.current
}

val LocalColorPalette = staticCompositionLocalOf<AppColorScheme> {
    error(message = "Something goes wrong with colors")
}

val LocalTypography = staticCompositionLocalOf<Typography> {
    error(message = "Something goes wrong with typography")
}

val LocalShapes = staticCompositionLocalOf<Shapes> {
    error(message = "Something goes wrong with shapes")
}