// presentation/ui/theme/Theme.kt
package com.lossabinos.serviceapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Color scheme personalizado para Los Sabinos Service App
 */
private val LightColorScheme = lightColorScheme(
    primary = PrimaryYellow,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryYellowDark,
    onPrimaryContainer = TextBlack,
    
    secondary = GrayIcon,
    onSecondary = Color.White,
    secondaryContainer = GrayIconLight,
    onSecondaryContainer = TextBlack,
    
    tertiary = InfoBlue,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD1E7FF),
    onTertiaryContainer = TextBlack,
    
    error = ErrorRed,
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = TextBlack,
    
    background = BackgroundLight,
    onBackground = TextBlack,
    
    surface = BackgroundWhite,
    onSurface = TextBlack,
    surfaceVariant = BorderGray,
    onSurfaceVariant = TextGray,
    
    outline = BorderGray,
    outlineVariant = GrayIconLight,
    
    scrim = Color.Black.copy(alpha = 0.32f),
    surfaceBright = Color.White,
    surfaceContainer = Color(0xFFF9F9F9),
    surfaceContainerHigh = Color(0xFFF5F5F5),
    surfaceContainerHighest = Color(0xFFEFEFEF),
    surfaceContainerLow = Color.White,
    surfaceContainerLowest = Color.White,
    surfaceDim = Color(0xFFE8E8E8)
)

/**
 * Tema personalizado para Los Sabinos Service App
 */
@Composable
fun LosabosTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
