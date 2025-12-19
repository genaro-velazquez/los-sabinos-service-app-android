package com.lossabinos.serviceapp.ui.components.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.components.atoms.ContinueButtonAtom

@Composable
fun VehicleRegistrationActionOrganism(
    onContinueClick: () -> Unit,
    isEnabled: Boolean = true
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ContinueButtonAtom(
            text = "Iniciar Captura",
            onClick = onContinueClick,
            enabled = isEnabled
        )
    }
}