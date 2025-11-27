// presentation/ui/components/atoms/ActionIcon.kt
package com.lossabinos.serviceapp.presentation.ui.components.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Atom: Icon de acción
 *
 * Icono circular con fondo color primario
 * Utilizado en ActionCard
 *
 * @param icon Vector del icono a mostrar
 * @param contentDescription Descripción del contenido para accesibilidad
 * @param modifier Modifier para personalización
 */
@Composable
fun ActionIcon(
    icon: ImageVector,
    contentDescription: String = "",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(56.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(32.dp)
        )
    }
}

/**
 * Previews
 */
@Preview
@Composable
fun ActionIconPreview() {
    ActionIcon(
        icon = Icons.Filled.Camera,
        contentDescription = "Camera Icon"
    )
}
