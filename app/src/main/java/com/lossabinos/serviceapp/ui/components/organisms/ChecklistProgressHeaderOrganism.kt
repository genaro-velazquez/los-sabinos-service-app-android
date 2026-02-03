package com.lossabinos.serviceapp.ui.components.organisms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.components.molecules.HeaderWithBadgeMolecule
import com.lossabinos.serviceapp.ui.components.molecules.ProgressIndicatorMolecule

@Composable
fun ChecklistProgressHeaderOrganism(
    serviceName: String,
    templateName: String,
    currentProgress: Int,  // ej: 1
    totalTasks: Int,       // ej: 4
    progressPercentage: Int,  // ej: 35
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(
            width = 1.dp,
            color =Color(0xFFE0E0E0)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ═══════════════════════════════════════
            // SECCIÓN 1: Header con Badge
            // ═══════════════════════════════════════
            HeaderWithBadgeMolecule(
                title = serviceName,
                subtitle = templateName,
                badge = "$currentProgress/$totalTasks"
            )

            Spacer(modifier = Modifier.height(1.dp))

            // ═══════════════════════════════════════
            // SECCIÓN 2: Progress Indicator
            // ═══════════════════════════════════════
            ProgressIndicatorMolecule(
                label = "Progreso General",
                percentage = progressPercentage
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ChecklistProgressHeaderOrganismPreview(){
    MaterialTheme{
        ChecklistProgressHeaderOrganism(
            serviceName = "Service name",
            templateName = "prueba",
            currentProgress = 4,
            totalTasks = 7,
            progressPercentage = 40
        )
    }
}
