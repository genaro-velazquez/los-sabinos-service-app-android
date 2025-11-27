// presentation/ui/components/atoms/SecondaryButton.kt
package com.lossabinos.serviceapp.ui.components.atoms

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lossabinos.serviceapp.ui.theme.BorderGray
import com.lossabinos.serviceapp.ui.theme.TextBlack

/**
 * Botón secundario con borde (outline)
 *
 * @param text Texto del botón
 * @param onClick Callback cuando se presiona
 * @param modifier Modifier para personalización
 * @param isEnabled Si el botón está habilitado
 * @param height Alto del botón (default: 56.dp)
 * @param borderColor Color del borde (default: BorderGray)
 * @param textColor Color del texto (default: TextBlack)
 * @param cornerRadius Radio de las esquinas (default: 12.dp)
 */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    height: Dp = 56.dp,
    borderColor: Color = BorderGray,
    textColor: Color = TextBlack,
    cornerRadius: Dp = 12.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .border(2.dp, borderColor, RoundedCornerShape(cornerRadius))
            .clickable(enabled = isEnabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}
