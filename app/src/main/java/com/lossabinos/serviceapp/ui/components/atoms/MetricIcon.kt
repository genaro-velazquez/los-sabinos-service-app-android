// presentation/ui/components/atoms/MetricIcon.kt
package com.lossabinos.serviceapp.ui.components.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.theme.StatusYellow
import com.lossabinos.serviceapp.ui.theme.StatusYellowLight

/**
 * Icono de métrica con fondo redondeado
 *
 * @param icon Vector del icono a mostrar
 * @param iconSize Tamaño del icono
 * @param backgroundColor Color de fondo
 * @param iconColor Color del icono
 * @param cornerRadius Radio de las esquinas
 * @param modifier Modifier para personalización
 */
@Composable
fun MetricIcon(
    icon: ImageVector = Icons.Default.CheckCircle,
    iconSize: Dp = 28.dp,
    backgroundColor: Color = StatusYellowLight,
    iconColor: Color = StatusYellow,
    cornerRadius: Dp = 12.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(56.dp)
            .background(backgroundColor, RoundedCornerShape(cornerRadius)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(iconSize)
        )
    }
}
