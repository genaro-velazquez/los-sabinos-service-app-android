// presentation/ui/components/molecules/ActionCard.kt
package com.lossabinos.serviceapp.presentation.ui.components.molecules

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.presentation.ui.components.atoms.ActionIcon
import com.lossabinos.serviceapp.presentation.ui.components.atoms.ActionTitle
import com.lossabinos.serviceapp.presentation.ui.components.atoms.ActionCardContainer

/**
 * Molecule: Action Card
 * 
 * Tarjeta completa con icono y título
 * Combina ActionIcon + ActionTitle en un ActionCardContainer
 * 
 * Estructura:
 * ┌─────────────────┐
 * │   [ICON]        │
 * │                 │
 * │   Título        │
 * └─────────────────┘
 *
 * @param title Texto del título
 * @param icon Icono a mostrar
 * @param onClick Callback cuando se presiona la tarjeta
 * @param modifier Modifier para personalización
 */
@Composable
fun ActionCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    ActionCardContainer(
        modifier = modifier
            .clickable(enabled = true, onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icono
            ActionIcon(
                icon = icon,
                contentDescription = title
            )
            
            // Título
            ActionTitle(
                text = title
            )
        }
    }
}

/**
 * Previews
 */
@Composable
fun ActionCardPreview() {
    ActionCard(
        title = "Cámara",
        icon = Icons.Filled.Camera,
        onClick = { }
    )
}

@Composable
fun ActionCardReportesPreview() {
    ActionCard(
        title = "Reportes",
        icon = Icons.Filled.BarChart,
        onClick = { }
    )
}

@Composable
fun ActionCardUbicacionPreview() {
    ActionCard(
        title = "Ubicación",
        icon = Icons.Filled.LocationOn,
        onClick = { }
    )
}
