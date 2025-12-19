package com.lossabinos.serviceapp.ui.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.components.molecules.HeaderWithSubtitleMolecule

@Composable
fun VehicleRegistrationHeaderOrganism() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .padding(vertical = 16.dp)
    ) {
        HeaderWithSubtitleMolecule(
            title = "Registro de Vehículo",
            subtitle = "Complete los datos para iniciar el servicio."
        )
    }
}