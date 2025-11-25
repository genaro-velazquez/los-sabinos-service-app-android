// presentation/ui/components/molecules/LogoCard.kt
package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.theme.ExtraLargeCorner
import com.lossabinos.serviceapp.ui.theme.GrayIconLight

/**
 * Componente Molecule - Tarjeta de Logo
 * Icono de cohete en un cuadrado redondeado
 */
@Composable
fun LogoCard(
    modifier: Modifier = Modifier,
    size: Int = 120,
    backgroundColor: Color = GrayIconLight,
    iconColor: Color = Color(0xFF999999)
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .background(backgroundColor, ExtraLargeCorner),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Home,
            contentDescription = "Logo Los Sabinos",
            tint = iconColor,
            modifier = Modifier.size((size / 2).dp)
        )
    }
}
