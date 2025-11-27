// presentation/ui/components/molecules/StatusSection.kt
package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.components.atoms.buttons.PrimaryButton
import com.lossabinos.serviceapp.ui.components.atoms.StatusText
import com.lossabinos.serviceapp.ui.theme.CardBackground

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
    statusText: String = "Estás en línea",
    lastSyncText: String = "Última sincronización: Hoy 10:45 AM",
    onSyncClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(CardBackground, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        StatusText(
            text = statusText,
            isMainText = true
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        StatusText(
            text = lastSyncText,
            isMainText = false
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        PrimaryButton(
            text = "Sincronizar",
            onClick = onSyncClick
        )
    }
}
