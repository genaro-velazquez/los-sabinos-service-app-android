// presentation/ui/components/organisms/MetricsSection.kt
package com.lossabinos.serviceapp.ui.components.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentLate
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.components.molecules.MetricCard
import com.lossabinos.serviceapp.ui.theme.StatusYellow
import com.lossabinos.serviceapp.ui.theme.StatusYellowLight

/**
 * Organism que organiza las métricas en un grid 2x2
 *
 * Métricas incluidas:
 * - Completado (checkmark)
 * - En Proceso (refresh)
 * - Pendientes (clipboard)
 * - Eficiencia (chart)
 *
 * @param completedCount Cantidad de servicios completados
 * @param inProgressCount Cantidad en progreso
 * @param pendingCount Cantidad pendiente
 * @param efficiencyPercentage Porcentaje de eficiencia
 * @param modifier Modifier para personalización
 */
@Composable
fun MetricsSection(
    completedCount: String = "12",
    inProgressCount: String = "3",
    pendingCount: String = "5",
    efficiencyPercentage: String = "92%",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Primera fila: Completado + En Proceso
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricCard(
                label = "Completado",
                value = completedCount,
                icon = Icons.Default.AssignmentTurnedIn,
                iconBackgroundColor = StatusYellowLight,
                iconColor = StatusYellow,
                modifier = Modifier.weight(1f)
            )
            
            MetricCard(
                label = "En Proceso",
                value = inProgressCount,
                icon = Icons.Default.Refresh,
                iconBackgroundColor = StatusYellowLight,
                iconColor = StatusYellow,
                modifier = Modifier.weight(1f)
            )
        }
        
        // Segunda fila: Pendientes + Eficiencia
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricCard(
                label = "Pendientes",
                value = pendingCount,
                icon = Icons.Default.AssignmentLate,
                iconBackgroundColor = StatusYellowLight,
                iconColor = StatusYellow,
                modifier = Modifier.weight(1f)
            )
            
            MetricCard(
                label = "Eficiencia",
                value = efficiencyPercentage,
                icon = Icons.Default.BarChart,
                iconBackgroundColor = StatusYellowLight,
                iconColor = StatusYellow,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
