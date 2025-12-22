package com.lossabinos.serviceapp.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import com.lossabinos.domain.entities.ServiceFieldValue
import com.lossabinos.domain.valueobjects.FieldType
import com.lossabinos.domain.valueobjects.ServiceField

data class VehicleRegistrationFieldUIModel(
    val id: String,
    val label: String,
    val value: String,
    val placeholder: String = "",
    val icon: ImageVector,
    val suffix: String = "",
    val keyboardType: KeyboardType = KeyboardType.Text,
    val additionalInfo: String = "",
    val fieldType: FieldTypeUIModel = FieldTypeUIModel.TEXT_INPUT,
    val required: Boolean = false
)

enum class FieldTypeUIModel {
    TEXT_INPUT,
    DROPDOWN,
    DATE_PICKER,
    NUMBER_INPUT
}

// Domain to presentation
fun ServiceField.toVehicleRegistrationFieldUIModel(value: String): VehicleRegistrationFieldUIModel {
    return VehicleRegistrationFieldUIModel(
        id = label.lowercase().replace(" ", "_"),
        label = label,
        value = value,
        placeholder = when (type) {
            "number" -> "000,000"
            else -> "Ingresa ${label.lowercase()}"
        },
        icon = when (label.lowercase()) {
            "kilometraje" -> Icons.Filled.Edit
            "tipo de aceite" -> Icons.Filled.Settings
            else -> Icons.Filled.Edit
        },
        suffix = when (label.lowercase()) {
            "kilometraje" -> "km"
            else -> ""
        },
        keyboardType = when (type) {
            "number" -> KeyboardType.Number
            else -> KeyboardType.Text
        },
        fieldType = when (type) {
            "number" -> FieldTypeUIModel.NUMBER_INPUT
            else -> FieldTypeUIModel.TEXT_INPUT
        },
        required = required
    )
}

fun VehicleRegistrationFieldUIModel.toDomain() : ServiceFieldValue{
    return ServiceFieldValue(
        id = id,
        label = label,
        value = value,
        fieldType = fieldType.toDomain(),
        required = required
    )
}

fun FieldTypeUIModel.toDomain() : FieldType{
    return when (this){
        FieldTypeUIModel.DATE_PICKER -> FieldType.DATE_PICKER
        FieldTypeUIModel.DROPDOWN -> FieldType.DROPDOWN
        FieldTypeUIModel.NUMBER_INPUT -> FieldType.NUMBER_INPUT
        FieldTypeUIModel.TEXT_INPUT -> FieldType.TEXT_INPUT
    }
}
