// presentation/ui/components/atoms/ModalTitle.kt
package com.lossabinos.serviceapp.ui.components.atoms

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.lossabinos.serviceapp.ui.theme.TextBlack

/**
 * Título del modal
 *
 * @param text Texto del título
 * @param modifier Modifier para personalización
 */
@Composable
fun ModalTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = TextBlack,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}
