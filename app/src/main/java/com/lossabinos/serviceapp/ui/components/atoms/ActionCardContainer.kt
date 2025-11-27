// presentation/ui/components/atoms/ActionCardContainer.kt
package com.lossabinos.serviceapp.presentation.ui.components.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Atom: Card Container para acciones
 *
 * Container base con fondo blanco, esquinas redondeadas y sombra
 * Utilizado como base para ActionCard
 *
 * @param modifier Modifier para personalización
 * @param content Contenido dentro de la tarjeta
 */
@Composable
fun ActionCardContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,  // ✅ CORREGIDO
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface  // ✅ CORREGIDO
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        content()
    }
}

/**
 * Preview
 */
@Preview
@Composable
fun ActionCardContainerPreview() {
    ActionCardContainer {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)  // ✅ CORREGIDO
        )
    }
}
