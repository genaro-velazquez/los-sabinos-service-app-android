// presentation/ui/components/molecules/UnsyncSection.kt
package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lossabinos.serviceapp.ui.components.atoms.buttons.PrimaryButton
import com.lossabinos.serviceapp.ui.components.atoms.StatusText
import com.lossabinos.serviceapp.ui.theme.CardBackground
import com.lossabinos.serviceapp.ui.theme.TextBlack
import com.lossabinos.serviceapp.ui.theme.TextGray

/**
 * Sección de datos sin sincronizar
 *
 * @param subtitle Subtítulo (ej: "Datos sin sincronizar")
 * @param title Título principal (ej: "4 Servicios")
 * @param details Detalles adicionales (ej: "2 Firmas, 8 Fotos, 1 Observación")
 * @param onSyncNowClick Callback para sincronizar ahora
 * @param modifier Modifier para personalización
 */
@Composable
fun UnsyncSection(
    subtitle: String = "Datos sin sincronizar",
    title: String = "4 Servicios",
    details: String = "2 Firmas, 8 Fotos, 1 Observación",
    onSyncNowClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(CardBackground, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        StatusText(
            text = subtitle,
            isMainText = false
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextBlack
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = details,
            fontSize = 14.sp,
            color = TextGray
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        PrimaryButton(
            text = "Sincronizar ahora",
            onClick = onSyncNowClick
        )
    }
}
