// presentation/ui/components/atoms/ActionTitle.kt
package com.lossabinos.serviceapp.presentation.ui.components.atoms

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
        maxLines = 1,  // ← NO se quiebra
        overflow = TextOverflow.Ellipsis  // ← Si es muy largo: "Reportes..."

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
