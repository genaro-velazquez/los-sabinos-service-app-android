package com.lossabinos.serviceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lossabinos.domain.entities.ServiceFieldValue
import com.lossabinos.domain.usecases.checklist.GetServiceFieldValuesUseCase
import com.lossabinos.domain.usecases.checklist.SaveServiceFieldValueUseCase
import com.lossabinos.domain.usecases.checklist.SaveServiceFieldValuesUseCase
import com.lossabinos.domain.valueobjects.Template
import com.lossabinos.serviceapp.models.ScanQRState
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

    // 🆕 Estado del QR
    private val _qrState = MutableStateFlow(ScanQRState.INITIAL)
    val qrState: StateFlow<ScanQRState> = _qrState.asStateFlow()

    private val _manualQRInput = MutableStateFlow("")
    val manualQRInput: StateFlow<String> = _manualQRInput.asStateFlow()
    private val _isValidatingManual = MutableStateFlow(false)
    val isValidatingManual: StateFlow<Boolean> = _isValidatingManual.asStateFlow()

    private val _showQRErrorAlert = MutableStateFlow(false)
    val showQRErrorAlert: StateFlow<Boolean> = _showQRErrorAlert.asStateFlow()

    private val _qrErrorMessage = MutableStateFlow("")
    val qrErrorMessage: StateFlow<String> = _qrErrorMessage.asStateFlow()

    fun dismissQRErrorAlert() {
        _showQRErrorAlert.value = false
        _qrErrorMessage.value = ""
    }

    fun changeQRCode() {
        _qrState.value = ScanQRState.INITIAL
        _manualQRInput.value = ""
        println("🔄 Volviendo a validar QR...")
    }

    // 🆕 Almacenar el vehicleId del servicio
    private var serviceVehicleId: String = ""

    // 🆕 Métodos para cambiar estado
    fun setValidQR() {
        _qrState.value = ScanQRState.VALID
        println("✅ QR Válido")
    }

    fun setInvalidQR() {
        _qrState.value = ScanQRState.INVALID
        println("❌ QR Inválido")
    }

    fun resetQRState() {
        _qrState.value = ScanQRState.INITIAL
        println("🔄 Reset QR State")
    }

    // 🆕 SIMPLIFICAR: Solo una función de validación
    fun setServiceVehicleId(vehicleId: String) {
        this.serviceVehicleId = vehicleId
        println("🚗 Vehicle ID del servicio establecido: $vehicleId")
    }

    fun updateManualQRInput(value: String) {
        _manualQRInput.value = value
        println("📝 QR manual: $value")
    }

    fun validateManualQRCode() {
        viewModelScope.launch {
            try {
                _isValidatingManual.value = true
                println("🔍 Validando QR manual: ${_manualQRInput.value}")

                val qrValue = _manualQRInput.value.trim()

                if (qrValue.isEmpty()) {
                    println("❌ Campo vacío")
                    _qrErrorMessage.value = "Por favor ingresa un código QR"
                    _showQRErrorAlert.value = true
                    _isValidatingManual.value = false
                    return@launch
                    /*
                    println("❌ Campo vacío")
                    _qrState.value = ScanQRState.INVALID
                    _isValidatingManual.value = false
                    return@launch
                    */
                }

                val qrVehicleId = extractVehicleIdFromQR(qrValue)

                println("📋 Vehicle ID extraído: $qrVehicleId")
                println("📋 Vehicle ID servicio: $serviceVehicleId")

                if (qrVehicleId.isNotEmpty() && qrVehicleId == serviceVehicleId) {
                    println("✅ QR Manual Válido")
                    _qrState.value = ScanQRState.VALID
                } else {
                    println("❌ QR Manual Inválido")
                    _qrErrorMessage.value = "El código QR no corresponde a este vehículo.\nIntenta de nuevo."
                    _showQRErrorAlert.value = true
                    _qrState.value = ScanQRState.INVALID
                }

                _isValidatingManual.value = false

            } catch (e: Exception) {
                println("❌ Error: ${e.message}")
                _qrErrorMessage.value = "Error al validar el código: ${e.message}"
                _showQRErrorAlert.value = true
                _qrState.value = ScanQRState.INVALID
                _isValidatingManual.value = false
                /*
                println("❌ Error: ${e.message}")
                _qrState.value = ScanQRState.INVALID
                _isValidatingManual.value = false
                */
            }
        }
    }

    // 🆕 Validar QR y actualizar estado
    fun validateQRAndLoadData(qrValue: String) {
        viewModelScope.launch {
            try {
                println("🔍 Validando QR: $qrValue")

                val qrVehicleId = extractVehicleIdFromQR(qrValue)

                println("📋 Vehicle ID extraído del QR: $qrVehicleId")
                println("📋 Vehicle ID del servicio: $serviceVehicleId")

                if (qrVehicleId.isNotEmpty() && qrVehicleId == serviceVehicleId) {
                    println("✅ QR Válido - Vehicle IDs coinciden")
                    _qrState.value = ScanQRState.VALID
                    _manualQRInput.value = qrValue
                } else {
                    println("❌ QR Inválido - Vehicle IDs no coinciden")
                    println("   QR: $qrVehicleId")
                    println("   Servicio: $serviceVehicleId")
                    _qrErrorMessage.value = "El código QR escaneado no corresponde a este vehículo.\nIntenta de nuevo."
                    _showQRErrorAlert.value = true
                    _qrState.value = ScanQRState.INVALID
                }
            } catch (e: Exception) {
                println("❌ Error validando QR: ${e.message}")
                _qrErrorMessage.value = "Error al procesar el QR: ${e.message}"
                _showQRErrorAlert.value = true
                _qrState.value = ScanQRState.INVALID
            }
        }
    }

    // 🆕 Extraer vehicle_id del string del QR
    private fun extractVehicleIdFromQR(qrValue: String?): String {
        val value = qrValue?.trim()

        return if (value.isNullOrBlank()) {
            println("⚠️ QR vacío o nulo")
            ""
        } else {
            value
        }
        /*
        return try {
            // Formato: "vehicle_id=be48febf-2858-4bae-bb8a-64e80c15bcee"
            if (qrValue.contains("vehicle_id=")) {
                val parts = qrValue.split("vehicle_id=")
                if (parts.size > 1) {
                    val extracted = parts[1].trim()
                    println("📝 Extracto del QR: $extracted")
                    extracted
                } else {
                    ""
                }
            } else {
                println("⚠️ QR no contiene formato 'vehicle_id='")
                ""
            }
        } catch (e: Exception) {
            println("❌ Error extrayendo vehicle_id: ${e.message}")
            ""
        }
        */
    }


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