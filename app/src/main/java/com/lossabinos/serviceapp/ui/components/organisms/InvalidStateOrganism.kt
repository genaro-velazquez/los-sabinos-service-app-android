package com.lossabinos.serviceapp.ui.components.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.components.molecules.ErrorCardMolecule
import com.lossabinos.serviceapp.ui.components.molecules.QRScannerHeaderMolecule

@Composable
fun InvalidStateOrganism(
    onScanClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Header con botón QR
        QRScannerHeaderMolecule(onScanClick = onScanClick)

        // Separador
        androidx.compose.material3.HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Error Card
        ErrorCardMolecule(
            title = "QR No Válido",
            message = "El código QR no es válido para este servicio. Intenta de nuevo o contacta al administrador.",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}
