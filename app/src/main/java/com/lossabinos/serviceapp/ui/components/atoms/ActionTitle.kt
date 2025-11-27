// presentation/ui/components/atoms/ActionTitle.kt
package com.lossabinos.serviceapp.presentation.ui.components.atoms

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

/**
 * Atom: Título de acción
 *
 * Texto para mostrar el nombre de la acción en ActionCard
 * Usa typography titleMedium del tema
 *
 * @param text Texto del título
 * @param modifier Modifier para personalización
 */
@Composable
fun ActionTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center
    )
}

/**
 * Preview
 */
@Preview
@Composable
fun ActionTitlePreview() {
    ActionTitle(text = "Cámara")
}
