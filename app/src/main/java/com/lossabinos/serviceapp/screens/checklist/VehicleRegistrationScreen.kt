package com.lossabinos.serviceapp.screens.checklist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
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
    navController: NavController  // 🆕 Agregar para navegar
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


/*
@Composable
fun VehicleRegistrationScreen(
    checklistTemplateJson: String,
    serviceId: String,
    onContinueClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel: VehicleRegistrationViewModel = hiltViewModel()

    // 🆕 Cargar campos dinámicamente al abrir
    LaunchedEffect(checklistTemplateJson) {
        // 1. Cargar campos del template
        viewModel.loadServiceFieldsFromJson(checklistTemplateJson)
        // 2. Cargar datos previos guardados
        viewModel.loadPreviousFieldValues(serviceId)
    }

    val serviceFields by viewModel.serviceFields.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isEnabled = viewModel.validateAndContinue() && !isLoading

    VehicleRegistrationTemplate(
        fields = serviceFields,  // 🆕 Usa los campos dinámicos
        onFieldChange = { fieldId, newValue ->
            viewModel.updateFieldValue(fieldId, newValue)
        },
        onContinueClick = {
            viewModel.saveVehicleData(
                assignedServiceId = serviceId
            ) {
                onContinueClick()
            }
        },
        onBackClick = onBackClick,
        isEnabled = isEnabled
    )
}
*/
