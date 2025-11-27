// presentation/ui/components/organisms/HomeHeaderSection.kt
package com.lossabinos.serviceapp.ui.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.components.molecules.UserHeader
import com.lossabinos.serviceapp.ui.theme.CardBackground

/**
 * Organism que combina UserHeader con contexto de Home
 *
 * Incluye:
 * - Avatar del usuario
 * - Nombre y ubicación
 * - Estado online
 * - Botones de settings y logout
 *
 * @param userName Nombre del usuario
 * @param userLocation Ubicación
 * @param isOnline Estado del usuario
 * @param onSettingsClick Callback para settings
 * @param onLogoutClick Callback para logout
 * @param modifier Modifier para personalización
 */
@Composable
fun HomeHeaderSection(
    userName: String = "Isabella Rodriguez",
    userLocation: String = "Mexico City",
    isOnline: Boolean = true,
    onSettingsClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    UserHeader(
        userName = userName,
        userLocation = userLocation,
        isOnline = isOnline,
        onSettingsClick = onSettingsClick,
        onLogoutClick = onLogoutClick,
        modifier = modifier
            .fillMaxWidth()
            .background(CardBackground, RoundedCornerShape(16.dp))
            .padding(16.dp)
    )
}
