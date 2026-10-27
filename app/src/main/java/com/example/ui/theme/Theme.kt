package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val FlowCareDarkColorScheme = darkColorScheme(
    primary = FlowTeal,
    onPrimary = CharcoalBlack,
    primaryContainer = FlowTealContainer,
    onPrimaryContainer = FlowTealLight,
    secondary = FlowTealLight,
    onSecondary = CharcoalBlack,
    secondaryContainer = CharcoalGrey,
    onSecondaryContainer = PureWhite,
    tertiary = StatusInfo,
    onTertiary = PureWhite,
    background = CharcoalBlack,
    onBackground = PureWhite,
    surface = CharcoalSurface,
    onSurface = PureWhite,
    surfaceVariant = CharcoalGrey,
    onSurfaceVariant = LightGrey,
    outline = CharcoalBorder,
    error = StatusDanger,
    onError = PureWhite
)

private val FlowCareLightColorScheme = lightColorScheme(
    primary = FlowTealDark,
    onPrimary = PureWhite,
    primaryContainer = Color(0xFFCCFBF1),
    onPrimaryContainer = Color(0xFF115E59),
    secondary = FlowTeal,
    onSecondary = PureWhite,
    secondaryContainer = Color(0xFFE4E4E7),
    onSecondaryContainer = CharcoalBlack,
    tertiary = StatusInfo,
    onTertiary = PureWhite,
    background = Color(0xFFF8FAFC),
    onBackground = CharcoalBlack,
    surface = PureWhite,
    onSurface = CharcoalBlack,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = CharcoalGrey,
    outline = Color(0xFFCBD5E1),
    error = StatusDanger,
    onError = PureWhite
)

@Composable
fun FlowCareTheme(
    darkTheme: Boolean = true, // Default to Charcoal Black aesthetic requested by user
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) FlowCareDarkColorScheme else FlowCareLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
