// presentation/ui/components/molecules/UserHeader.kt
package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lossabinos.serviceapp.ui.components.atoms.ActionButton
import com.lossabinos.serviceapp.ui.components.atoms.Avatar
import com.lossabinos.serviceapp.ui.components.atoms.StatusBadge
import com.lossabinos.serviceapp.ui.theme.TextBlack
import com.lossabinos.serviceapp.ui.theme.TextGray

/**
 * Header del usuario con nombre, ubicación y acciones
 *
 * @param userName Nombre del usuario
 * @param userLocation Ubicación del usuario
 * @param isOnline Si el usuario está online
 * @param onSettingsClick Callback para settings
 * @param onLogoutClick Callback para logout
 * @param modifier Modifier para personalización
 */
@Composable
fun UserHeader(
    userName: String = "Isabella Rodriguez",
    userLocation: String = "Mexico City",
    isOnline: Boolean = true,
    onSettingsClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar + Nombre + Ubicación (con StatusBadge)
        Box(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Avatar()

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = userName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextBlack
                    )
                    Text(
                        text = userLocation,
                        fontSize = 14.sp,
                        color = TextGray
                    )
                }
            }

            // Status badge en la esquina superior derecha
            StatusBadge(
                isOnline = isOnline,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }

        // Botones de acción
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ActionButton(
                icon = Icons.Default.Notifications,
                onClick = onSettingsClick,
                contentDescription = "Configuración"
            )
            ActionButton(
                icon = Icons.AutoMirrored.Filled.Logout,
                onClick = onLogoutClick,
                contentDescription = "Cerrar sesión"
            )
        }
    }
}

