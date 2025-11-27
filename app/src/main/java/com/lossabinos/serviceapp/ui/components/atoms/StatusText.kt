// presentation/ui/components/atoms/StatusText.kt
package com.lossabinos.serviceapp.ui.components.atoms

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.lossabinos.serviceapp.ui.theme.TextBlack
import com.lossabinos.serviceapp.ui.theme.TextGray

/**
 * Texto de estado
 *
 * @param text Texto a mostrar
 * @param isMainText Si es texto principal (más grande y bold)
 * @param color Color del texto
 * @param modifier Modifier para personalización
 */
@Composable
fun StatusText(
    text: String,
    isMainText: Boolean = true,
    color: Color = if (isMainText) TextBlack else TextGray,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = color,
        fontSize = if (isMainText) 16.sp else 14.sp,
        fontWeight = if (isMainText) FontWeight.SemiBold else FontWeight.Normal,
        modifier = modifier
    )
}
