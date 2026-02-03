package com.lossabinos.serviceapp.screens.checklist

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.lossabinos.serviceapp.ui.components.templates.VehicleRegistrationTemplate
import com.lossabinos.serviceapp.viewmodel.MechanicsViewModel
import com.lossabinos.serviceapp.viewmodel.VehicleRegistrationViewModel

@Composable
fun VehicleRegistrationScreen(
    checklistTemplateJson: String,
    serviceId: String,
    onContinueClick: () -> Unit,
    onBackClick: () -> Unit,
    navController: NavController
) {
    val viewModel: VehicleRegistrationViewModel = hiltViewModel()

    // 🆕 Obtener el vehicleId del servicio
    val mechanicsViewModel: MechanicsViewModel = hiltViewModel()
    val services = mechanicsViewModel.assignedServices
        .collectAsStateWithLifecycle().value
    val selectedService = services.find { it.assignedService.id == serviceId }


    LaunchedEffect(checklistTemplateJson, serviceId) {
        viewModel.loadServiceFieldsFromJson(checklistTemplateJson)
        viewModel.loadPreviousFieldValues(serviceId)

        // 🆕 Establecer el vehicleId del servicio en el ViewModel
        selectedService?.let { service ->
            viewModel.setServiceVehicleId(service.assignedService.vehicle.vin)  // Asumiendo que tiene vehicleId
            println("🚗 VehicleId configurado en ViewModel: ${service.assignedService.vehicle.id}")
        }

    }

    // 🆕 Estados
    val qrState by viewModel.qrState.collectAsState()
    val serviceFields by viewModel.serviceFields.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isEnabled = viewModel.validateAndContinue() && !isLoading
    val manualQRValue by viewModel.manualQRInput.collectAsState()
    val isValidatingManual by viewModel.isValidatingManual.collectAsState()
    val showQRErrorAlert by viewModel.showQRErrorAlert.collectAsState()
    val qrErrorMessage by viewModel.qrErrorMessage.collectAsState()

    if (showQRErrorAlert) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissQRErrorAlert() },
            title = {
                Text(
                    "❌ Código Inválido",
                    color = Color(0xFFC62828),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = qrErrorMessage,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.dismissQRErrorAlert() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFC62828)
                    )
                ) {
                    Text("Reintentar", color = Color.White)
                }
            }
        )
    }

    VehicleRegistrationTemplate(
        qrState = qrState,
        fields = serviceFields,
        manualQRValue = manualQRValue,
        onManualQRChange = { newValue ->
            viewModel.updateManualQRInput(newValue)
        },
        onScanClick = {
            println("🔍 Abriendo escáner QR")
            // 🆕 Navegar a QR Scanner
            navController.navigate("qr_scanner")
        },
        onValidateManualClick = {
            println("✅ Validando QR manual")
            viewModel.validateManualQRCode()
        },
        onChangeQRClick = {  // ← AGREGAR ESTO
            println("🔄 Cambiando QR")
            viewModel.changeQRCode()
        },
        onFieldChange = { fieldId, newValue ->
            viewModel.updateFieldValue(fieldId, newValue)
        },
        onContinueClick = {
            viewModel.saveVehicleData(
                assignedServiceId = serviceId,
                onSuccess = onContinueClick
            )
        },
        onBackClick = {
            // 🆕 Resetear QR state al regresar
            viewModel.resetQRState()
            onBackClick()
        },
        isEnabled = isEnabled,
        isValidatingManual = isValidatingManual
    )
}
