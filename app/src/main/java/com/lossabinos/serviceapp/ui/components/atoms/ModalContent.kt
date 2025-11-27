// presentation/ui/components/atoms/ModalContent.kt
package com.lossabinos.serviceapp.ui.components.atoms

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.lossabinos.serviceapp.ui.theme.TextGray

/**
 * Contenido/descripción del modal
 *
 * @param text Texto del contenido
 * @param modifier Modifier para personalización
 */
@Composable
fun ModalContent(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 16.sp,
        color = TextGray,
        textAlign = TextAlign.Center,
        modifier = modifier,
        lineHeight = 24.sp
    )
}
