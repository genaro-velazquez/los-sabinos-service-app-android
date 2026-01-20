package com.lossabinos.serviceapp.ui.components.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lossabinos.serviceapp.ui.components.atoms.ScanQRButtonAtom
import com.lossabinos.serviceapp.ui.components.molecules.ManualQRInputMolecule

@Composable
fun QRValidationOrganism(
    manualQRValue: String,
    onManualQRChange: (String) -> Unit,
    onScanClick: () -> Unit,
    onValidateManualClick: () -> Unit,
    isValidatingManual: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Título
        Text(
            text = "Validar Vehículo",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color.Black
        )

        // Subtítulo
        Text(
            text = "Escanea o ingresa el código QR del vehículo",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF757575),
            modifier = Modifier.padding(horizontal = 16.dp),
            textAlign = TextAlign.Center
        )

        // ════════════════════════════════════════════════
        // OPCIÓN 1: ESCANEAR QR
        // ════════════════════════════════════════════════
        ScanQRButtonAtom(onClick = onScanClick)

        // ════════════════════════════════════════════════
        // DIVISOR: O
        // ════════════════════════════════════════════════
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = Color(0xFFE0E0E0)
            )
            Text(
                "O",
                color = Color(0xFF9E9E9E),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = Color(0xFFE0E0E0)
            )
        }

        // ════════════════════════════════════════════════
        // OPCIÓN 2: INGRESO MANUAL
        // ════════════════════════════════════════════════
        ManualQRInputMolecule(
            qrValue = manualQRValue,
            onValueChange = onManualQRChange,
            onValidateClick = onValidateManualClick,
            isValidating = isValidatingManual,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}
