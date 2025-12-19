package com.lossabinos.serviceapp.screens.checklist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.lossabinos.serviceapp.ui.components.templates.VehicleRegistrationTemplate
import com.lossabinos.serviceapp.viewmodel.VehicleRegistrationViewModel

@Composable
fun VehicleRegistrationScreen(
    checklistTemplateJson: String,
    onContinueClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel: VehicleRegistrationViewModel = hiltViewModel()

    // 🆕 Cargar campos dinámicamente al abrir
    LaunchedEffect(checklistTemplateJson) {
        viewModel.loadServiceFieldsFromJson(checklistTemplateJson)
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
            viewModel.saveVehicleData {
                onContinueClick()
            }
        },
        onBackClick = onBackClick,
        isEnabled = isEnabled
    )
}
