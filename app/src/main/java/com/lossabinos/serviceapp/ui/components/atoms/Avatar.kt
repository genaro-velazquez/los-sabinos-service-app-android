// presentation/ui/components/atoms/Avatar.kt
package com.lossabinos.serviceapp.ui.components.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.theme.AvatarBrown
import com.lossabinos.serviceapp.ui.theme.GrayIcon

/**
 * Avatar circular para usuario
 *
 * @param size Tamaño del avatar (default: 56.dp)
 * @param backgroundColor Color de fondo (default: AvatarBrown)
 * @param iconColor Color del icono (default: GrayIcon)
 * @param modifier Modifier para personalización
 */
@Composable
fun Avatar(
    size: Dp = 56.dp,
    backgroundColor: Color = AvatarBrown,
    iconColor: Color = GrayIcon,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .background(backgroundColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Avatar usuario",
            tint = iconColor,
            modifier = Modifier.size(size * 0.5f)
        )
    }
}
