// presentation/ui/components/molecules/StatusSection.kt
package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.models.ui.HomeSyncStatusUiState
import com.lossabinos.serviceapp.ui.components.atoms.buttons.PrimaryButton
import com.lossabinos.serviceapp.ui.components.atoms.StatusText
import com.lossabinos.serviceapp.ui.theme.CardBackground
import com.lossabinos.serviceapp.utils.toHomeDateLabel

/**
 * Sección de estado (Estás en línea + última sincronización)
 *
 * @param statusText Texto de estado (ej: "Estás en línea")
 * @param lastSyncText Texto de última sincronización
 * @param onSyncClick Callback para el botón de sincronización
 * @param modifier Modifier para personalización
 */
@Composable
fun StatusSection(
    uiState: HomeSyncStatusUiState,
    onSyncClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(CardBackground, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {

        val isOnline = uiState.isOnline

        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(CardBackground, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {

            // ─────────────────────────────────────
            // Indicador + texto de estado
            // ─────────────────────────────────────
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Circle,
                    contentDescription = null,
                    tint = if (isOnline) Color(0xFF4CAF50) else Color(0xFFF44336),
                    modifier = Modifier.size(10.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                StatusText(
                    text = if (isOnline) "Estás en línea" else "Sin conexión",
                    isMainText = true
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // ─────────────────────────────────────
            // Texto de última sincronización
            // ─────────────────────────────────────
            val lastSyncText = when {
                uiState.lastSyncAt != null ->
                    "Última actualización: ${uiState.lastSyncAt.toHomeDateLabel()}"
                isOnline ->
                    "Aún no se ha sincronizado"
                else ->
                    "Sin sincronizaciones previas"
            }

            StatusText(
                text = lastSyncText,
                isMainText = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ─────────────────────────────────────
            // Botón con color según estado
            // ─────────────────────────────────────
            PrimaryButton(
                text = "Sincronizar",
                onClick = onSyncClick,
                enabled = isOnline,
                backgroundColor = if (isOnline) {
                    Color(0xFFFFC107) // amarillo normal
                } else {
                    Color(0xFFE0E0E0) // gris deshabilitado
                },
                textColor = if (isOnline) {
                    Color.Black
                } else {
                    Color(0xFF9E9E9E)
                }
            )
        }

        /*
                StatusText(
                    text = if (uiState.isOnline) "Estás en línea" else "Sin conexión",
                    isMainText = true
                )

                Spacer(modifier = Modifier.height(4.dp))

                val lastSyncText = when {
                    uiState.lastSyncAt != null ->
                        "Última actualización: ${uiState.lastSyncAt.toHomeDateLabel()}"
                    uiState.isOnline ->
                        "Aún no se ha sincronizado"
                    else ->
                        "Sin sincronizaciones previas"
                }

                StatusText(
                    text = lastSyncText,
                    isMainText = false
                )

                Spacer(modifier = Modifier.height(16.dp))

                PrimaryButton(
                    text = "Sincronizar",
                    onClick = onSyncClick
                )
         */
    }
}
