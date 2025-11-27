package com.lossabinos.serviceapp.ui.components.atoms.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lossabinos.serviceapp.ui.theme.PrimaryYellow
import androidx.compose.ui.graphics.Color
import com.lossabinos.serviceapp.ui.theme.OnPrimary


/**
 * Botón principal amarillo con soporte para loading state
 *
 * Características:
 * - Estado loading con spinner
 * - Completamente customizable (colores, tamaño, radio)
 * - Accesible y responsive
 *
 * @param text Texto del botón
 * @param onClick Callback cuando se presiona
 * @param modifier Modifier para personalización
 * @param enabled Si el botón está habilitado
 * @param isLoading Si está en estado de carga
 * @param height Alto del botón (default: 56.dp)
 * @param backgroundColor Color de fondo (default: PrimaryYellow)
 * @param textColor Color del texto (default: OnPrimary)
 * @param cornerRadius Radio de las esquinas (default: 12.dp)
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    height: Dp = 56.dp,
    backgroundColor: Color = PrimaryYellow,
    textColor: Color = OnPrimary,
    cornerRadius: Dp = 12.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(backgroundColor, RoundedCornerShape(cornerRadius))
            .clickable(enabled = enabled && !isLoading, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = textColor,
                    strokeWidth = 2.dp
                )
                Text(
                    "Cargando...",
                    color = textColor,
                    fontSize = 14.sp
                )
            }
        } else {
            Text(
                text,
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
        }
    }
}
