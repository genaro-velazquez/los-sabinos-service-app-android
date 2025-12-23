package com.lossabinos.serviceapp.ui.components.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lossabinos.serviceapp.ui.components.atoms.ScanQRButtonAtom

@Composable
fun InitialStateOrganism(
    onScanClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
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
            text = "Escanea el código QR del vehículo para continuar",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF757575),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // Botón QR
        ScanQRButtonAtom(onClick = onScanClick)
    }
}
