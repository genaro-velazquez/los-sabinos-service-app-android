// presentation/ui/components/organisms/SyncSection.kt
package com.lossabinos.serviceapp.ui.components.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.components.molecules.StatusSection
import com.lossabinos.serviceapp.ui.components.molecules.UnsyncSection

/**
 * Organism que combina las secciones de sincronización
 *
 * Incluye:
 * - StatusSection: Estado online + última sincronización
 * - UnsyncSection: Datos pendientes por sincronizar
 *
 * @param statusText Texto de estado (ej: "Estás en línea")
 * @param lastSyncText Última sincronización
 * @param unsyncTitle Título de datos sin sincronizar (ej: "4 Servicios")
 * @param unsyncDetails Detalles (ej: "2 Firmas, 8 Fotos")
 * @param onSyncClick Callback para sincronizar
 * @param onSyncNowClick Callback para sincronizar ahora
 * @param modifier Modifier para personalización
 */
@Composable
fun SyncSection(
    statusText: String = "Estás en línea",
    lastSyncText: String = "Última sincronización: Hoy 10:45 AM",
    unsyncTitle: String = "4 Servicios",
    unsyncDetails: String = "2 Firmas, 8 Fotos, 1 Observación",
    onSyncClick: () -> Unit = {},
    onSyncNowClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Estado online + última sincronización
        StatusSection(
            statusText = statusText,
            lastSyncText = lastSyncText,
            onSyncClick = onSyncClick,
            modifier = Modifier.fillMaxWidth()
        )
        
        // Datos sin sincronizar
        UnsyncSection(
            subtitle = "Datos sin sincronizar",
            title = unsyncTitle,
            details = unsyncDetails,
            onSyncNowClick = onSyncNowClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
