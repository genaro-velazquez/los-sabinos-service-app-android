// presentation/ui/theme/Color.kt
package com.lossabinos.serviceapp.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Paleta de colores para Los Sabinos Service App
 * Basado en el diseño de la pantalla de login y home
 */

// ============================================================================
// PRIMARY COLORS
// ============================================================================

val PrimaryYellow = Color(0xFFFFCA3D)        // Botones principales
val PrimaryYellowDark = Color(0xFFFDD835)    // Variación oscura

val PrimaryBlue = Color(0xFF1F88D9)          // Border principal (Home)
val PrimaryBlueDark = Color(0xFF1565C0)      // Azul oscuro para variaciones

// ============================================================================
// BACKGROUND COLORS
// ============================================================================

val BackgroundLight = Color(0xFFF5F5F5)      // Fondo principal
val BackgroundWhite = Color(0xFFFFFFFF)      // Fondo de campos
val ContainerBackground = Color(0xFFF0F0F0)  // Gris claro para contenedores
val CardBackground = Color(0xFFFFFFFF)       // Blanco puro para cards
val SurfaceElevated = Color(0xFFFEFEFE)      // Cards con sombra
val SurfacePressed = Color(0xFFEEEEEE)       // Estados pressed

// ============================================================================
// TEXT COLORS
// ============================================================================

val TextBlack = Color(0xFF000000)             // Texto principal
val TextDark = Color(0xFF333333)              // Texto secundario
val TextGray = Color(0xFF666666)              // Texto terciario
val TextGrayLight = Color(0xFFCCCCCC)         // Placeholder/hint
val TextDisabled = Color(0xFF999999)          // Texto deshabilitado
val TextMuted = Color(0xFF9E9E9E)             // Texto muy gris

// ============================================================================
// NEUTRAL COLORS
// ============================================================================

val GrayIcon = Color(0xFF666666)              // Iconos
val GrayIconLight = Color(0xFFE0E0E0)         // Iconos claros
val BorderGray = Color(0xFFE8E8E8)            // Bordes
val DividerColor = Color(0xFFE0E0E0)          // Líneas divisoras

// ============================================================================
// AVATAR COLORS
// ============================================================================

val AvatarBrown = Color(0xFFD4A574)           // Beige para avatares
val AvatarBrownLight = Color(0xFFE8CFBB)      // Beige más claro

// ============================================================================
// STATUS COLORS
// ============================================================================

val SuccessGreen = Color(0xFF4CAF50)           // Éxito / Online
val ErrorRed = Color(0xFFE63946)               // Error
val WarningOrange = Color(0xFFFFA500)          // Advertencia
val InfoBlue = Color(0xFF1976D2)               // Información

// Status específicos para Home
val StatusYellow = Color(0xFFFFCA3D)           // Amarillo para checkmarks
val StatusYellowLight = Color(0xFFFFE082)      // Amarillo claro
val StatusPending = Color(0xFFFFC107)          // Pendiente (amarillo)
val StatusInProgress = Color(0xFF2196F3)       // En progreso (azul)
val StatusCompleted = Color(0xFF4CAF50)        // Completado (verde)
val OnlineGreen = Color(0xFF4CAF50)            // Verde para online

// ============================================================================
// ACCENT COLORS
// ============================================================================

val AccentOrange = Color(0xFFFF9800)           // Acentos secundarios

// ============================================================================
// SEMANTIC COLORS
// ============================================================================

val OnPrimary = Color(0xFF000000)              // Texto sobre amarillo
val OnBackground = Color(0xFF333333)           // Texto sobre fondo
val OnSurface = Color(0xFF000000)              // Texto sobre superficie
val OnError = Color(0xFFFFFFFF)                // Texto sobre error
