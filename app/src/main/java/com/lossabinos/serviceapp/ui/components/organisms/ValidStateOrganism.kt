package com.lossabinos.serviceapp.ui.components.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.models.VehicleRegistrationFieldUIModel
import com.lossabinos.serviceapp.ui.components.molecules.QRScannerHeaderMolecule

@Composable
fun ValidStateOrganism(
    fields: List<VehicleRegistrationFieldUIModel>,
    onScanClick: () -> Unit,
    onFieldChange: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header con botón QR
        QRScannerHeaderMolecule(onScanClick = onScanClick)

        // Separador
        androidx.compose.material3.HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Formulario
        VehicleRegistrationFormOrganism(
            fields = fields,
            onFieldChange = onFieldChange,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}
