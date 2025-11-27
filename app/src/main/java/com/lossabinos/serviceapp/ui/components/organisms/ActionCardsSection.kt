// presentation/ui/components/organisms/ActionCardsSection.kt
package com.lossabinos.serviceapp.presentation.ui.components.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.lossabinos.serviceapp.presentation.ui.components.molecules.ActionCard

/**
 * Data class para representar una acción
 *
 * @param id Identificador único
 * @param title Título de la acción
 * @param icon Icono a mostrar
 * @param onClick Callback cuando se presiona
 */
data class ActionCardModel(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit = {}
)

/**
 * Organism: Action Cards Section
 * 
 * Sección completa que muestra múltiples tarjetas de acción en grid
 * Combina múltiples ActionCard molecules en un layout responsivo
 * 
 * Características:
 * - Grid automático (responde al tamaño de pantalla)
 * - Espaciado consistente
 * - Padding configurable
 * - Título opcional
 * - Animaciones suaves
 *
 * @param actions Lista de acciones a mostrar
 * @param title Título opcional de la sección
 * @param onActionClick Callback cuando se presiona una tarjeta
 * @param columns Número de columnas en el grid (default: 3)
 * @param modifier Modifier para personalización
 */
@Composable
fun ActionCardsSection(
    actions: List<ActionCardModel>,
    title: String? = null,
    onActionClick: (String) -> Unit = {},
    columns: Int = 3,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        // Título opcional
        if (title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,  // ✅ CORREGIDO
                color = MaterialTheme.colorScheme.onBackground,   // ✅ CORREGIDO
                modifier = Modifier.padding(bottom = 0.dp)
            )
        }

        // Grid de tarjetas
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            modifier = Modifier.fillMaxWidth()
                .height(150.dp),
            contentPadding = PaddingValues(0.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(
                items = actions,
                key = { it.id }
            ) { action ->
                ActionCard(
                    title = action.title,
                    icon = action.icon,
                    onClick = {
                        action.onClick()
                        onActionClick(action.id)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/**
 * Preview con datos de ejemplo
 */
@Preview
@Composable
fun ActionCardsSectionPreview() {
    val actions = listOf(
        ActionCardModel(
            id = "camera",
            title = "Cámara",
            icon = Icons.Filled.Camera,
            onClick = { }
        ),
        ActionCardModel(
            id = "reports",
            title = "Reportes",
            icon = Icons.Filled.BarChart,
            onClick = { }
        ),
        ActionCardModel(
            id = "location",
            title = "Ubicación",
            icon = Icons.Filled.LocationOn,
            onClick = { }
        )
    )
    
    ActionCardsSection(
        actions = actions,
        title = "Acciones Rápidas",
        onActionClick = { actionId ->
            println("Action clicked: $actionId")
        }
    )
}

/**
 * Preview responsive - 2 columnas
 */
@Composable
fun ActionCardsSectionPreview2Columns() {
    val actions = listOf(
        ActionCardModel(
            id = "camera",
            title = "Cámara",
            icon = Icons.Filled.Camera
        ),
        ActionCardModel(
            id = "reports",
            title = "Reportes",
            icon = Icons.Filled.BarChart
        ),
        ActionCardModel(
            id = "location",
            title = "Ubicación",
            icon = Icons.Filled.LocationOn
        )
    )
    
    ActionCardsSection(
        actions = actions,
        columns = 2,
        title = "Acciones"
    )
}
