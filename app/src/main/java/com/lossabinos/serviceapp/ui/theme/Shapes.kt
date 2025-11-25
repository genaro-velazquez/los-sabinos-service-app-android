// presentation/ui/theme/Shapes.kt
package com.lossabinos.serviceapp.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Formas personalizadas para Los Sabinos Service App
 */

val AppShapes = Shapes(
    // Esquinas pequeñas (inputs, pequeños elementos)
    small = RoundedCornerShape(4.dp),
    
    // Esquinas medianas (cards, campos)
    medium = RoundedCornerShape(12.dp),
    
    // Esquinas grandes (botones, grandes contenedores)
    large = RoundedCornerShape(16.dp)
)

// Shapes personalizados adicionales
val ExtraSmallCorner = RoundedCornerShape(2.dp)
val SmallCorner = RoundedCornerShape(8.dp)
val MediumCorner = RoundedCornerShape(12.dp)
val LargeCorner = RoundedCornerShape(16.dp)
val ExtraLargeCorner = RoundedCornerShape(24.dp)
val FullRounded = RoundedCornerShape(50.dp)  // Para hacer círculos
