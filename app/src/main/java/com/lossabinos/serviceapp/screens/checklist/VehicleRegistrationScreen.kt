package com.lossabinos.serviceapp.screens.checklist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lossabinos.serviceapp.ui.components.templates.VehicleRegistrationTemplate
import com.lossabinos.serviceapp.viewmodel.VehicleRegistrationViewModel

@Composable
fun VehicleRegistrationScreen(
    checklistTemplateJson: String,
    serviceId: String,
    onContinueClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel: VehicleRegistrationViewModel = hiltViewModel()

    LaunchedEffect(checklistTemplateJson) {
        viewModel.loadServiceFieldsFromJson(checklistTemplateJson)
        viewModel.loadPreviousFieldValues(serviceId)
    }

    // 🆕 Estados
    val qrState by viewModel.qrState.collectAsState()
    val serviceFields by viewModel.serviceFields.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isEnabled = viewModel.validateAndContinue() && !isLoading

    VehicleRegistrationTemplate(
        qrState = qrState,  // 🆕 Pasar estado
        fields = serviceFields,
        onScanClick = {
            println("🔍 Escanear QR")
            // TODO: Implementar lógica de QR
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
        onBackClick = onBackClick,
        isEnabled = isEnabled
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
