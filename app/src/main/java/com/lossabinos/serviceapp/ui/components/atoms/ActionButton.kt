// presentation/ui/components/atoms/ActionButton.kt
package com.lossabinos.serviceapp.ui.components.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.theme.CardBackground
import com.lossabinos.serviceapp.ui.theme.GrayIcon
import com.lossabinos.serviceapp.ui.theme.GrayIconLight

/**
 * Botón circular para acciones (Settings, Logout, etc)
 *
 * @param icon Icono a mostrar
 * @param onClick Callback cuando se presiona
 * @param size Tamaño del botón
 * @param backgroundColor Color de fondo
 * @param iconColor Color del icono
 * @param contentDescription Descripción accesibilidad
 * @param modifier Modifier para personalización
 */
@Composable
fun ActionButton(
    icon: ImageVector = Icons.Default.Settings,
    onClick: () -> Unit = {},
    size: Dp = 48.dp,
    backgroundColor: Color = CardBackground,
    iconColor: Color = GrayIcon,
    contentDescription: String? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .background(backgroundColor, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconColor,
            modifier = Modifier.size(size * 0.5f)
        )
    }
}
