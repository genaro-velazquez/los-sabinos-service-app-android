package com.lossabinos.serviceapp.ui.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lossabinos.domain.entities.Observation
import com.lossabinos.serviceapp.models.ObservationModel
import com.lossabinos.serviceapp.ui.components.atoms.SectionTitleAtom
import com.lossabinos.serviceapp.ui.components.molecules.ObservationInputMolecule
import com.lossabinos.serviceapp.ui.components.molecules.ObservationItemMolecule

@Composable
fun ObservationsOrganism(
    observations: List<ObservationModel>,  // ← CAMBIAR de String a List
    observationResponses: Map<String, String> = emptyMap(),  // ← ID → Valor
    onObservationChange: (String, String) -> Unit = { _, _ -> },  // ← CAMBIAR callback
    modifier: Modifier = Modifier,
    maxCharacters: Int = 300
) {
    if (observations.isEmpty()) {
        return
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFFAFAFA),
                shape = RoundedCornerShape(8.dp)
            ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Título
        SectionTitleAtom(
            title = "OBSERVACIONES"
        )

        // Iterar sobre cada observación
        observations.forEach { observation ->
            val currentValue = observationResponses[observation.id] ?: ""

            ObservationItemMolecule(
                id = observation.id,
                description = observation.description,
                responseType = observation.responseType.name,
                value = currentValue,
                onValueChange = { newValue ->
                    onObservationChange(observation.id, newValue)
                },
                requiresResponse = observation.requiresResponse
            )

            // Separador entre items
            if (observation != observations.last()) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = Color(0xFFE0E0E0)
                )
            }
        }
    }
}

/*
@Composable
fun ObservationsOrganism(
    observations: String,
    onObservationsChange: (String) -> Unit,
    maxCharacters: Int = 300,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = Color(0xFFFAFAFA),  // Fondo gris claro
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ═══════════════════════════════════════
        // Título
        // ═══════════════════════════════════════
        SectionTitleAtom(
            title = "Observaciones"
        )

        // ═══════════════════════════════════════
        // Input + Contador
        // ═══════════════════════════════════════
        ObservationInputMolecule(
            value = observations,
            onValueChange = onObservationsChange,
            maxCharacters = maxCharacters
        )
    }
}
 */