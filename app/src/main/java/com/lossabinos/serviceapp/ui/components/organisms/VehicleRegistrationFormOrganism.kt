package com.lossabinos.serviceapp.ui.components.organisms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lossabinos.serviceapp.models.VehicleRegistrationFieldUIModel
import com.lossabinos.serviceapp.ui.components.atoms.IconWithBackgroundAtom
import com.lossabinos.serviceapp.ui.components.atoms.InputFieldAtom
import com.lossabinos.serviceapp.ui.components.atoms.SubtextAtom


@Composable
fun VehicleRegistrationFormOrganism(
    fields: List<VehicleRegistrationFieldUIModel> = emptyList(),
    onFieldChange: (fieldId: String, newValue: String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFE0E0E0)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 🆕 Iterar sobre los campos dinámicamente
            fields.forEachIndexed { index, field ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Header del campo
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconWithBackgroundAtom(
                            iconRes = field.icon,
                            backgroundColor = Color(0xFFFFF3CD),
                            iconColor = Color(0xFFFFC107),
                            size = 48.dp
                        )
                        Text(
                            text = field.label,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    // Divider (excepto en el último)
                    if (index < fields.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp),
                            color = Color(0xFFE0E0E0)
                        )
                    }

                    // Input field
                    InputFieldAtom(
                        value = field.value,
                        onValueChange = { newValue ->
                            onFieldChange(field.id, newValue)
                        },
                        placeholder = field.placeholder,
                        keyboardType = field.keyboardType,
                        suffix = field.suffix
                    )

                    // Additional info (si tiene)
                    if (field.additionalInfo.isNotEmpty()) {
                        SubtextAtom(text = field.additionalInfo)
                    }
                }
            }
        }
    }
}

/*
@Composable
fun VehicleRegistrationFormOrganism(
    kilometrageValue: String,
    onKilometrageChange: (String) -> Unit,
    oilTypeValue: String,
    onOilTypeChange: (String) -> Unit,
    lastKilometers: String = "45,230 km",
    modifier: Modifier = Modifier
) {
        Card(
            modifier = modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(
                width = 1.dp,
                color =Color(0xFFE0E0E0)
            )
        ) {
            KilometrageCardMolecule(
                value = kilometrageValue,
                onValueChange = onKilometrageChange,
                lastKilometers = lastKilometers
            )

            OilTypeCardMolecule(
                value = oilTypeValue,
                onValueChange = onOilTypeChange
            )
        }
}

@Preview(showBackground = true)
@Composable
fun VehicleRegistrationFormOrganismPreview(){
    MaterialTheme{
        VehicleRegistrationFormOrganism(
           kilometrageValue = "10,000",
            onKilometrageChange = {},
            oilTypeValue = "sintético",
            onOilTypeChange = {}
        )
    }
}
*/