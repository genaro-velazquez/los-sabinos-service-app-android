package com.lossabinos.serviceapp.ui.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.components.atoms.SectionTitleAtom
import com.lossabinos.serviceapp.ui.components.molecules.ObservationInputMolecule

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