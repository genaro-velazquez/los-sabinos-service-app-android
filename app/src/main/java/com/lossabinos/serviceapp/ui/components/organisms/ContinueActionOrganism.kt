package com.lossabinos.serviceapp.ui.components.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.components.molecules.ContinueButtonMolecule

@Composable
fun ContinueActionOrganism(
    onClick: () -> Unit,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ═══════════════════════════════════════
        // Botón Continuar
        // ═══════════════════════════════════════
        ContinueButtonMolecule(
            text = "Continuar",
            onClick = onClick,
            enabled = enabled,
            isLoading = isLoading
        )
    }
}