package com.lossabinos.serviceapp.ui.components.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lossabinos.serviceapp.models.VehicleRegistrationFieldUIModel
import com.lossabinos.serviceapp.ui.components.organisms.VehicleRegistrationActionOrganism
import com.lossabinos.serviceapp.ui.components.organisms.VehicleRegistrationFormOrganism

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleRegistrationTemplate(
    fields: List<VehicleRegistrationFieldUIModel> = emptyList(),  // 🆕 Nuevo parámetro
    onFieldChange: (fieldId: String, newValue: String) -> Unit = { _, _ -> },  // 🆕 Nuevo
    onContinueClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    isEnabled: Boolean = true
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Registro de Vehículo",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
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
                .padding(paddingValues),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // 🆕 Pasa la lista de campos
                VehicleRegistrationFormOrganism(
                    fields = fields,
                    onFieldChange = onFieldChange,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            VehicleRegistrationActionOrganism(
                onContinueClick = onContinueClick,
                isEnabled = isEnabled
            )
        }
    }
}
