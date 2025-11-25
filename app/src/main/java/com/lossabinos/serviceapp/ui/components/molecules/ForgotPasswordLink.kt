// presentation/ui/components/molecules/ForgotPasswordLink.kt
package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import com.lossabinos.serviceapp.ui.theme.PrimaryYellow

/**
 * Componente Molecule - Enlace de Contraseña Olvidada
 * Texto clickeable para recuperar contraseña
 */
@Composable
fun ForgotPasswordLink(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "Olvidé mi contraseña",
    color: Color = PrimaryYellow
) {
    Text(
        text = text,
        modifier = modifier.clickable { onClick() },
        color = color,
        style = MaterialTheme.typography.bodyMedium,
        textDecoration = TextDecoration.None,
        textAlign = TextAlign.Center
    )
}
