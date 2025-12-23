package com.lossabinos.serviceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lossabinos.data.local.mappers.toEntity
import com.lossabinos.domain.entities.ServiceFieldValue
import com.lossabinos.domain.usecases.checklist.GetServiceFieldValuesUseCase
import com.lossabinos.domain.usecases.checklist.SaveServiceFieldValueUseCase
import com.lossabinos.domain.usecases.checklist.SaveServiceFieldValuesUseCase
import com.lossabinos.domain.valueobjects.Template
import com.lossabinos.serviceapp.models.VehicleRegistrationFieldUIModel
import com.lossabinos.serviceapp.models.toDomain
import com.lossabinos.serviceapp.models.toVehicleRegistrationFieldUIModel
import com.lossabinos.serviceapp.navigation.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject


/**
 * Estado de VehicleRegistration
 */
data class VehicleRegistrationState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * Eventos de VehicleRegistration
 */
sealed class VehicleRegistrationEvent {
    data class StartActivityCapture(val serviceId: String) : VehicleRegistrationEvent()
}

@HiltViewModel
class VehicleRegistrationViewModel @Inject constructor(
    private val saveServiceFieldValueUseCase: SaveServiceFieldValueUseCase,
    private val saveServiceFieldValuesUseCase: SaveServiceFieldValuesUseCase,
    private val getServiceFieldValuesUseCase: GetServiceFieldValuesUseCase
) : ViewModel() {

    private val _kilometrage = MutableStateFlow("")
    val kilometrage: StateFlow<String> = _kilometrage.asStateFlow()

    private val _oilType = MutableStateFlow("")
    val oilType: StateFlow<String> = _oilType.asStateFlow()

    private val _serviceFields = MutableStateFlow<List<VehicleRegistrationFieldUIModel>>(emptyList())
    val serviceFields: StateFlow<List<VehicleRegistrationFieldUIModel>> = _serviceFields.asStateFlow()

    private val _lastKilometers = MutableStateFlow("45,230 km")
    val lastKilometers: StateFlow<String> = _lastKilometers.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _navigationEvent = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvent: StateFlow<NavigationEvent?> = _navigationEvent.asStateFlow()

    fun updateKilometrage(value: String) {
        // Validar que sea solo números
        if (value.all { it.isDigit() }) {
            _kilometrage.value = value
        }
    }

    fun updateOilType(value: String) {
        _oilType.value = value
    }

    fun oEvent(event: VehicleRegistrationEvent){
        when(event){
            is VehicleRegistrationEvent.StartActivityCapture -> {
                println("✅ Navegando a ChecklistScreen: ${event.serviceId}")
                //_navigationEvent.value = NavigationEvent.NavigateToChecklistProgress(event.serviceId)
                _navigationEvent.value = NavigationEvent.NavigateToChecklistProgress(event.serviceId)
            }
        }
    }

    // 🆕 NUEVO: Cargar campos desde el JSON
    fun loadServiceFieldsFromJson(checklistTemplateJson: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                // Deserializar el JSON completo
                val checklistRoot = Json.decodeFromString<Template>(checklistTemplateJson)

                // Extraer serviceFields del template
                val serviceFieldsJson = checklistRoot.serviceFields

                println("📋 Service Fields encontrados: ${serviceFieldsJson.size}")
                serviceFieldsJson.forEach { field ->
                    println("   - ${field.label} (${field.type}, requerido: ${field.required})")
                }

                // 🆕 Convertir a VehicleRegistrationField
                val convertedFields = serviceFieldsJson.map { serviceFieldJson ->
                    val value = when (serviceFieldJson.label.lowercase()) {
                        "kilometraje" -> _kilometrage.value
                        "tipo de aceite" -> _oilType.value
                        else -> ""
                    }
                    serviceFieldJson.toVehicleRegistrationFieldUIModel(value)
                }

                // Guardar en el state
                _serviceFields.value = convertedFields

                println("✅ ${convertedFields.size} campos cargados dinámicamente")
                convertedFields.forEach { field ->
                    println("   - ID: ${field.id}, Label: ${field.label}, Required: ${field.required}")
                }

                _isLoading.value = false
            } catch (e: Exception) {
                println("❌ Error cargando serviceFields: ${e.message}")
                e.printStackTrace()
                _isLoading.value = false
            }
        }
    }

    fun updateFieldValue(fieldId: String, newValue: String) {
        when (fieldId) {
            "kilometraje" -> _kilometrage.value = newValue
            "tipo_de_aceite" -> _oilType.value = newValue
            else -> println("⚠️ Campo desconocido: $fieldId")
        }

        // 🆕 Actualizar el campo en la lista
        val updatedFields = _serviceFields.value.map { field ->
            if (field.id == fieldId) {
                field.copy(value = newValue)
            } else {
                field
            }
        }
        _serviceFields.value = updatedFields
    }

    fun validateAndContinue(): Boolean {
        // Validar que todos los campos requeridos estén completos
        return _serviceFields.value.all { field ->
            !field.required || field.value.isNotEmpty()
        }
    }

    fun saveVehicleData(
        assignedServiceId: String,
        onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                println("💾 Guardando datos del vehículo...")
                println("   Servicio: $assignedServiceId")
                println("   Campos: ${_serviceFields.value.size}")

                // 🆕 Convertir VehicleRegistrationFieldUIModel → ServiceFieldValue (Domain)
                val domainFields = _serviceFields.value.mapIndexed { index, uiField ->
                    ServiceFieldValue(
                        id = "0",
                        assignedServiceId = assignedServiceId,
                        fieldIndex = index,
                        fieldLabel = uiField.label,
                        value = uiField.value,
                        fieldType =  uiField.fieldType.toDomain(),  // "TEXT_INPUT", "NUMBER_INPUT"
                        required = uiField.required)
                }

                // 🆕 Llamar al UseCase (no directamente al Repository)
                saveServiceFieldValuesUseCase(
                    assignedServiceId = assignedServiceId,
                    fields = domainFields
                )

                println("✅ vehicleRegistrationViewMoedel - Datos del vehículo guardados:")
                domainFields.forEach { field ->
                    println("   - ${field.fieldLabel}: ${field.value}")
                }

                delay(500)
                _isLoading.value = false

                // 🆕 Emitir evento de navegación
                _navigationEvent.value = NavigationEvent.NavigateToChecklistProgress(assignedServiceId)

                onSuccess()

/*
                // TODO: Guardar datos en Room
                val fieldData = _serviceFields.value.associate { it.id to it.value }
                println("✅ Datos Obtenidos:")
                fieldData.forEach { (id, value) ->
                    println("   - $id: $value")
                }

                delay(500)
                _isLoading.value = false
                onSuccess()*/
            } catch (e: Exception) {
                println("❌ Error: ${e.message}")
                _isLoading.value = false
            }
        }
    }

    fun clearNavigationEvent() {
        // Limpiar si es necesario
        _navigationEvent.value = null
    }

    fun loadPreviousFieldValues(
        assignedServiceId: String
    ){
        viewModelScope.launch {
            try {
                println("💾 Cargando datos previos del servicio: $assignedServiceId")

                // Obtener del repository
                val savedValues = getServiceFieldValuesUseCase.invoke(
                    assignedServiceId = assignedServiceId
                )
                    //.map { it.toEntity() }

                if (savedValues.isNotEmpty()) {
                    println("✅ Datos previos encontrados: ${savedValues.size}")

                    // Actualizar los campos con los datos guardados
                    val updatedFields = _serviceFields.value.map { field ->
                        val savedValue = savedValues.find {
                            it.fieldLabel == field.label
                        }?.value

                        if (savedValue != null) {
                            println("   - ${field.label}: $savedValue")
                            field.copy(value = savedValue)
                        } else {
                            field
                        }
                    }

                    _serviceFields.value = updatedFields
                    println("✅ Campos actualizados con datos previos")
                } else {
                    println("⚠️ No hay datos previos guardados")
                }


            }catch (e: Exception){
                println("❌ Error: ${e.message}")
                _isLoading.value = false
            }

        }
    }

}