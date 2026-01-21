package com.lossabinos.serviceapp.ui.components.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lossabinos.serviceapp.models.ScanQRState
import com.lossabinos.serviceapp.models.VehicleRegistrationFieldUIModel
import com.lossabinos.serviceapp.ui.components.atoms.ContinueButtonAtom
import com.lossabinos.serviceapp.ui.components.molecules.QRConfirmedMolecule
import com.lossabinos.serviceapp.ui.components.organisms.InitialStateOrganism
import com.lossabinos.serviceapp.ui.components.organisms.InvalidStateOrganism
import com.lossabinos.serviceapp.ui.components.organisms.QRValidationOrganism
import com.lossabinos.serviceapp.ui.components.organisms.ValidStateOrganism
import com.lossabinos.serviceapp.ui.components.organisms.VehicleRegistrationActionOrganism
import com.lossabinos.serviceapp.ui.components.organisms.VehicleRegistrationFormOrganism

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleRegistrationTemplate(
    qrState: ScanQRState,
    fields: List<VehicleRegistrationFieldUIModel> = emptyList(),
    manualQRValue: String = "",
    onManualQRChange: (String) -> Unit = { _ -> },
    onScanClick: () -> Unit = {},
    onValidateManualClick: () -> Unit = {},
    onChangeQRClick: () -> Unit = {},
    onFieldChange: (String, String) -> Unit = { _, _ -> },
    onContinueClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    isEnabled: Boolean = true,
    isValidatingManual: Boolean = false
) {

    val scrollState = rememberScrollState()  // ← AGREGAR

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Vehículo") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAFAFA))
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState),  // ← AGREGAR ESTO
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🆕 Mostrar contenido según el estado
            when (qrState) {
                ScanQRState.INITIAL -> {
                    QRValidationOrganism(
                        manualQRValue = manualQRValue,
                        onManualQRChange = onManualQRChange,
                        onScanClick = onScanClick,
                        onValidateManualClick = onValidateManualClick,
                        isValidatingManual = isValidatingManual
                    )
                    /*
                    // Solo botón
                    InitialStateOrganism(
                        onScanClick = onScanClick
                    )
                    */
                }

                ScanQRState.VALID -> {
                    // ← MOSTRAR QR CONFIRMADO
                    QRConfirmedMolecule(
                        qrCode = manualQRValue,
                        onChangeClick = onChangeQRClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )

                    // Mostrar formulario
                    ValidStateOrganism(
                        fields = fields,
                        onScanClick = onScanClick,
                        onFieldChange = onFieldChange
                    )

                    // Botón continuar (al final)
                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.height(32.dp))

                    ContinueButtonAtom(
                        text = "Iniciar Captura",
                        onClick = onContinueClick,
                        enabled = isEnabled
                    )

                    /*
                    // Botón + Formulario
                    ValidStateOrganism(
                        fields = fields,
                        onScanClick = onScanClick,
                        onFieldChange = onFieldChange
                    )

                    // Botón continuar (al final)
                    Spacer(
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(
                        modifier = Modifier.height(32.dp)
                    )

                    ContinueButtonAtom(
                        text = "Iniciar Captura",
                        onClick = onContinueClick,
                        enabled = isEnabled
                    )
                    */
                }

                ScanQRState.INVALID -> {
                    QRValidationOrganism(
                        manualQRValue = manualQRValue,
                        onManualQRChange = onManualQRChange,
                        onScanClick = onScanClick,
                        onValidateManualClick = onValidateManualClick,
                        isValidatingManual = isValidatingManual
                    )

                    /*
                                        // ← CAMBIO: Mostrar error + permitir reintentar
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(
                                                    color = Color(0xFFFFCDD2),
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Error,
                                                contentDescription = "Error",
                                                tint = Color(0xFFC62828),
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Column {
                                                Text(
                                                    "Código Inválido",
                                                    color = Color(0xFF6D1C0E),
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 14.sp
                                                )
                                                Text(
                                                    "No corresponde a este vehículo",
                                                    color = Color(0xFF8D4031),
                                                    fontSize = 12.sp
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(16.dp))

                                        // Permitir reintentar con ambas opciones
                                        QRValidationOrganism(
                                            manualQRValue = manualQRValue,
                                            onManualQRChange = onManualQRChange,
                                            onScanClick = onScanClick,
                                            onValidateManualClick = onValidateManualClick,
                                            isValidatingManual = isValidatingManual
                                        )

                     */
                }
            }
        }
    }
}