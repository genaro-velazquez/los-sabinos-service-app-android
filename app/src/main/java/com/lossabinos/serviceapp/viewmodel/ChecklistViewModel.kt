package com.lossabinos.serviceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lossabinos.data.local.database.entities.ActivityEvidenceEntity
import com.lossabinos.data.local.database.entities.ActivityProgressEntity
import com.lossabinos.data.local.mappers.toEntity
//import com.lossabinos.data.repositories.local.ChecklistRepository
import com.lossabinos.domain.entities.ActivityEvidence
import com.lossabinos.domain.usecases.checklist.CompleteActivityUseCase
import com.lossabinos.domain.usecases.checklist.GetActivitiesProgressForSectionUseCase
import com.lossabinos.domain.usecases.checklist.GetEvidenceForActivityUseCase
import com.lossabinos.domain.usecases.checklist.GetObservationResponsesForSectionUseCase
import com.lossabinos.domain.usecases.checklist.GetTotalCompletedActivitiesUseCase
import com.lossabinos.domain.usecases.checklist.SaveActivityEvidenceUseCase
import com.lossabinos.domain.usecases.checklist.SaveObservationResponseUseCase
import com.lossabinos.domain.usecases.checklist.SaveServiceFieldValueUseCase
import com.lossabinos.domain.usecases.checklist.SaveServiceFieldValuesUseCase
import com.lossabinos.domain.valueobjects.Template
import com.lossabinos.serviceapp.models.ActivityModel
import com.lossabinos.serviceapp.models.ActivityUIModel
import com.lossabinos.serviceapp.models.ObservationUIModel
import com.lossabinos.serviceapp.models.SectionModel
import com.lossabinos.serviceapp.models.SectionUIModel
import com.lossabinos.serviceapp.models.VehicleRegistrationFieldUIModel
import com.lossabinos.serviceapp.models.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

data class ChecklistUIState(
    val currentSectionIndex: Int = 0,
    val totalSections: Int = 0,
    val currentSectionName: String = "",
    val templateName:String = "",
    val currentSectionActivities: List<ActivityUIModel> = emptyList(),  // ✨ NUEVO: Con UI Model
    val currentSectionObservations: List<ObservationUIModel> = emptyList(),  // ✨ NUEVO
    // Progreso GLOBAL (todo el servicio)
    val totalActivities: Int = 0,
    val completedActivities: Int = 0,
    val progressPercentage: Int = 0,
    // 🆕 AGREGAMOS: Progreso POR SECCIÓN
    val sectionTotalActivities: Int = 0,      // Tareas en la sección actual
    val sectionCompletedActivities: Int = 0,  // Tareas completadas en la sección actual
    val sectionProgressPercentage: Int = 0,   // Progreso SOLO de esta sección

    val isLoading: Boolean = false,
    val observations: String = "",
    val canContinue: Boolean = false,
    val allSectionsComplete: Boolean = false
)


@HiltViewModel
class ChecklistViewModel @Inject constructor(
//    private val checklistRepository: ChecklistRepository,
    private val completeActivityUseCase: CompleteActivityUseCase,
    private val getTotalCompletedActivitiesUseCase: GetTotalCompletedActivitiesUseCase,
    private val getObservationResponsesForSectionUseCase: GetObservationResponsesForSectionUseCase,
    private val getActivitiesProgressForSectionUseCase:  GetActivitiesProgressForSectionUseCase,
    private val getEvidenceForActivityUseCase: GetEvidenceForActivityUseCase,
    private val saveActivityEvidenceUseCase: SaveActivityEvidenceUseCase,
    private val saveObservationResponseUseCase: SaveObservationResponseUseCase
) : ViewModel() {
/*
    /***** Service Field Value *****/
    private val _kilometrage = MutableStateFlow("")
    val kilometrage: StateFlow<String> = _kilometrage.asStateFlow()
    private val _oilType = MutableStateFlow("")
    val oilType: StateFlow<String> = _oilType.asStateFlow()
    private val _serviceFields = MutableStateFlow<List<VehicleRegistrationFieldUIModel>>(emptyList())
    val serviceFields: StateFlow<List<VehicleRegistrationFieldUIModel>> = _serviceFields.asStateFlow()
    /********************************/

    fun saveVehicleRegistration(
        assignedServiceId: String,
        uiFields: List<VehicleRegistrationFieldUIModel>
    ){
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val domainFields = uiFields.map { it.toDomain() }
                saveServiceFieldValuesUseCase.invoke(
                    assignedServiceId = assignedServiceId,
                    fields = domainFields
                )
            }
            catch (e: Exception){
                _isLoading.value = false
                println("❌ Error: ${e.message}")
            }
        }
    }

    fun saveVehicleData(
        assignedServiceId: String,
        fieldIndex: Int,
        fieldLabel: String,
        fieldType: String,
        required: Boolean,
        value: String?
    ){
        viewModelScope.launch {
            try {
                _isLoading.value = true
                println("💾 Guardando datos del vehículo...")
                println("   Servicio: $assignedServiceId")
                println("   Campos: ${_serviceFields.value.size}")

                // 🆕 Guardar en Room usando el UseCase
                saveServiceFieldValueUseCase.invoke(
                    assignedServiceId = assignedServiceId,
                    fieldIndex = fieldIndex,
                    fieldLabel = fieldLabel,
                    fieldType = fieldType,
                    required = required,
                    value = value
                )
                _isLoading.value = false
            }
            catch (e: Exception) {
                _isLoading.value = false
                println("❌ Error: ${e.message}")
            }
        }
    }
*/

    private val _state = MutableStateFlow(ChecklistUIState())
    val state: StateFlow<ChecklistUIState> = _state.asStateFlow()

    private val _observations = MutableStateFlow("")
    val observations: StateFlow<String> = _observations.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var template: Template? = null
    private var serviceId:String = ""

    fun updateTaskProgress(serviceId: String, taskId: String, completed: Boolean) {
        viewModelScope.launch {
            try {
                println("✅ Task $taskId: $completed (serviceId: $serviceId)")
                // TODO: Implementar lógica de guardado en BD
            } catch (e: Exception) {
                println("❌ Error: ${e.message}")
            }
        }
    }

    fun capturePhoto(serviceId: String, taskId: String) {
        viewModelScope.launch {
            println("📷 Capturando foto para task $taskId")
            // TODO: Implementar captura de cámara
        }
    }

    fun selectPhoto(serviceId: String, taskId: String) {
        viewModelScope.launch {
            println("📁 Seleccionando foto para task $taskId")
            // TODO: Implementar selección de galería
        }
    }

    // ═══════════════════════════════════════════════════════
    // 1. CARGAR TEMPLATE + DATOS PREVIOS DE ROOM
    // ═══════════════════════════════════════════════════════
    fun loadTemplate(checklistTemplateJson: String, serviceIdParam: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                serviceId = serviceIdParam

                println("🔄 Deserializando template...")

                // ✨ PASO 1: Deserializar JSON → Domain
                template = Json.decodeFromString(checklistTemplateJson)

                template?.let { tmpl ->
                    println("✅ Template deserializado: ${tmpl.sections.size} secciones")

                    // ✨ PASO 2: Cargar progreso previo de Room
                    //val totalCompleted = checklistRepository.getTotalCompletedActivities(serviceId)
                    val totalCompleted = getTotalCompletedActivitiesUseCase.invoke(assignedServiceId = serviceId)
                    println("✅ Actividades completadas previas: $totalCompleted")

                    // ✨ PASO 3: Encontrar sección actual
                    val currentSectionIndex = findFirstIncompleteSection(tmpl, serviceId)
                    println("✅ Sección actual a mostrar: $currentSectionIndex")

                    // ✨ PASO 4: Cargar datos de sección actual (Domain + Room)
                    val sectionUIModel = loadSectionUIModel(
                        template = tmpl,
                        sectionIndex = currentSectionIndex,
                        serviceId = serviceId
                    )

                    // ✨ PASO 5: Cargar observaciones previas
                    val observationResponses = getObservationResponsesForSectionUseCase.invoke(
                        assignedServiceId = serviceId,
                        sectionIndex = currentSectionIndex
                    )
/*
                    val observationResponses = checklistRepository.getObservationResponsesForSection(
                        assignedServiceId = serviceId,
                        sectionIndex = currentSectionIndex
                    )

                    val previousObservations = observationResponses.firstOrNull()?.response ?: ""
*/
                    val previousObservations = observationResponses.firstOrNull() ?: ""

                    // ✨ PASO 6: Calcular totales
                    val totalActivities = sectionUIModel.activities.count() //tmpl.sections.sumOf { it.activities.size }
                    val progressPercentage = (totalCompleted * 100) / totalActivities

                    // 🆕 CALCULA PROGRESO POR SECCIÓN
                    val sectionTotalActivities = sectionUIModel.activities.size
                    val sectionCompletedActivities = sectionUIModel.activities.count {
                        it.progress?.completed == true
                    }
                    val sectionProgressPercentage = if (sectionTotalActivities > 0) {
                        (sectionCompletedActivities * 100) / sectionTotalActivities
                    } else {
                        0
                    }

                    // ✨ PASO 7: Actualizar estado
                    _state.value = ChecklistUIState(
                        currentSectionIndex = currentSectionIndex,
                        totalSections = tmpl.sections.size,
                        currentSectionName = sectionUIModel.section.name,
                        templateName = tmpl.name,
                        currentSectionActivities = sectionUIModel.activities,
                        //currentSectionObservations = emptyList(), /*sectionUIModel.observations*/,
                        totalActivities = totalActivities,
                        completedActivities = totalCompleted,
                        progressPercentage = progressPercentage,
                        sectionTotalActivities = sectionTotalActivities,
                        sectionCompletedActivities = sectionCompletedActivities,
                        sectionProgressPercentage = sectionProgressPercentage,
                        canContinue = checkIfSectionComplete(sectionUIModel.activities),
                        observations = "" //previousObservations
                    )

                    _observations.value = ""//previousObservations

                    println("✅ Estado actualizado:")
                    println("   - Sección: ${sectionUIModel.section.name}")
                    println("   - Actividades: ${sectionUIModel.activities.size}")
                    println("   - Completadas: $totalCompleted")
                    println("   - Progreso: $progressPercentage%")
                    //println("   - Observaciones previas: ${previousObservations.take(30)}...")
                }

                _isLoading.value = false
            } catch (e: Exception) {
                println("❌ Error cargando template: ${e.message}")
                e.printStackTrace()
                _isLoading.value = false
            }
        }
    }

    // ═══════════════════════════════════════════════════════
    // 2. CARGAR DATOS DE UNA SECCIÓN (Domain + Room)
    // ═══════════════════════════════════════════════════════
    private suspend fun loadSectionUIModel(
        template: Template,
        sectionIndex: Int,
        serviceId: String
    ): SectionUIModel {
        val section = template.sections[sectionIndex]

        // Cargar progreso de actividades desde Room
        val activitiesProgress = getActivitiesProgressForSectionUseCase.invoke(
            assignedServiceId = serviceId,
            sectionIndex = sectionIndex
        )
/*
        // Cargar progreso de actividades desde Room
        val activitiesProgress = checklistRepository.getActivitiesProgressForSection(
            assignedServiceId = serviceId,
            sectionIndex = sectionIndex
        )
*/
        // Cargar evidencias desde Room
        //val allEvidences = mutableMapOf<Int, List<ActivityEvidenceEntity>>()
        val allEvidences = mutableMapOf<Int, List<ActivityEvidence>>()
        activitiesProgress.forEach { progress ->
            allEvidences[progress.activityIndex] = getEvidenceForActivityUseCase.invoke(
                activityProgressId = progress.id
            )
/*
                        allEvidences[progress.activityIndex] = checklistRepository.getEvidenceForActivity(
                            activityProgressId = progress.id
                        )
*/
        }

        // Combinar Domain + Room
        val activitiesUI = section.activities.mapIndexed { index, activity ->
            val progress = activitiesProgress.find { it.activityIndex == index }
            val evidence = allEvidences[index] ?: emptyList()

            println("   - Actividad $index: ${activity.description}")
            println("     • Completada: ${progress?.completed ?: false}")
            println("     • Fotos: ${evidence.size}")

            ActivityUIModel(
                activity = ActivityModel(activity = activity),
                progress = progress?.toEntity(),
                evidence = evidence.map { it.toEntity() }
            )
        }

        val observationResponses = getObservationResponsesForSectionUseCase.invoke(
            assignedServiceId = serviceId,
            sectionIndex = sectionIndex
        )

/*
        // Cargar observaciones desde Room
        val observationResponses = checklistRepository.getObservationResponsesForSection(
            assignedServiceId = serviceId,
            sectionIndex = sectionIndex
        )
*/
        val observationsUI = section.observations.mapIndexed { index, observation ->
            val response = observationResponses.find { it.observationIndex == index }

            println("   - Observación $index: ${observation}")
            //println("     • Respuesta: ${response?.response?.take(30) ?: "Sin respuesta"}")
            //println("     • Respuesta: ${response?.response?.take(30) ?: "Sin respuesta"}")

            ObservationUIModel(
                observation = observation,
                response = response?.toEntity()
            )
        }

        return SectionUIModel(
            section = SectionModel(section = section),
            activities = activitiesUI//observationsUI
        )
    }

    // ═══════════════════════════════════════════════════════
    // 3. ENCONTRAR PRIMERA SECCIÓN INCOMPLETA
    // ═══════════════════════════════════════════════════════
    private suspend fun findFirstIncompleteSection(
        template: Template,
        serviceId: String
    ): Int {
        for ((index, section) in template.sections.withIndex()) {
            val activitiesProgress = getActivitiesProgressForSectionUseCase.invoke(
                assignedServiceId = serviceId,
                sectionIndex = index
            )
/*
            val activitiesProgress = checklistRepository.getActivitiesProgressForSection(
                assignedServiceId = serviceId,
                sectionIndex = index
            )
*/
            val completedCount = activitiesProgress.count { it.completed }
            val totalInSection = section.activities.size

            if (completedCount < totalInSection) {
                return index  // Sección incompleta encontrada
            }
        }
        return 0  // Todas completas, mostrar primera
    }

    // ═══════════════════════════════════════════════════════
    // 4. VERIFICAR SI SECCIÓN ESTÁ COMPLETA
    // ═══════════════════════════════════════════════════════
    private fun checkIfSectionComplete(activities: List<ActivityUIModel>): Boolean {
        return activities.all { it.progress?.completed == true }
    }

    // ═══════════════════════════════════════════════════════
    // 5. MARCAR ACTIVIDAD COMPLETADA
    // ═══════════════════════════════════════════════════════
    fun completeActivity(activityIndex: Int) {
        viewModelScope.launch {
            try {
                val state = _state.value
                val activity = state.currentSectionActivities[activityIndex]

                println("💾 Guardando actividad $activityIndex: ${activity.activity.description}")

                // Guardar en Room
                val progressId = completeActivityUseCase.invoke(
                    assignedServiceId = serviceId,
                    sectionIndex = state.currentSectionIndex,
                    activityIndex = activityIndex,
                    description = activity.activity.description,
                    requiresEvidence = activity.activity.requiresEvidence
                )
/*
                val progressId = checklistRepository.saveActivityProgress(
                    assignedServiceId = serviceId,
                    sectionIndex = state.currentSectionIndex,
                    activityIndex = activityIndex,
                    description = activity.activity.description,
                    requiresEvidence = activity.activity.requiresEvidence
                )
 */

                // Actualizar en memoria
                val updatedActivities = state.currentSectionActivities.toMutableList()
                updatedActivities[activityIndex] = activity.copy(
                    progress = ActivityProgressEntity(
                        id = progressId,
                        assignedServiceId = serviceId,
                        sectionIndex = state.currentSectionIndex,
                        activityIndex = activityIndex,
                        activityDescription = activity.activity.description,
                        requiresEvidence = activity.activity.requiresEvidence,
                        completed = true
                    )
                )

                // Recalcular
                //val totalCompleted = checklistRepository.getTotalCompletedActivities(serviceId)
                val totalCompleted = getTotalCompletedActivitiesUseCase.invoke(assignedServiceId = serviceId)
                val newPercentage = (totalCompleted * 100) / state.totalActivities
                val canContinue = checkIfSectionComplete(updatedActivities)

                // 🆕 RECALCULA PROGRESO DE SECCIÓN
                val newSectionCompletedActivities = updatedActivities.count {
                    it.progress?.completed == true
                }
                val newSectionProgressPercentage = if (state.sectionTotalActivities > 0) {
                    (newSectionCompletedActivities * 100) / state.sectionTotalActivities
                } else {
                    0
                }

                _state.value = state.copy(
                    currentSectionActivities = updatedActivities,
                    completedActivities = totalCompleted,
                    progressPercentage = newPercentage,
                    sectionCompletedActivities = newSectionCompletedActivities,
                    sectionProgressPercentage = newSectionProgressPercentage,
                    canContinue = canContinue
                )

                println("✅ Actividad guardada. Progreso: $totalCompleted/${state.totalActivities}")
            } catch (e: Exception) {
                println("❌ Error: ${e.message}")
            }
        }
    }

    // ═══════════════════════════════════════════════════════
    // 6. GUARDAR FOTO A ACTIVIDAD
    // ═══════════════════════════════════════════════════════
    fun addPhotoToActivity(activityIndex: Int, photoUri: String) {
        viewModelScope.launch {
            try {
                val state = _state.value
                val activity = state.currentSectionActivities[activityIndex]

                activity.progress?.let { progress ->
                    println("📷 Guardando foto para actividad $activityIndex: $photoUri")


                    saveActivityEvidenceUseCase.invoke(
                        id = 0,
                        activityProgressId = progress.id,
                        filePath = photoUri,
                        fileType = "image"
                    )
/*
                    checklistRepository.saveActivityEvidence(
                        activityProgressId = progress.id,
                        filePath = photoUri,
                        fileType = "image"
                    )
 */

                    println("✅ Foto guardada")
                }
            } catch (e: Exception) {
                println("❌ Error guardando foto: ${e.message}")
            }
        }
    }

    // ═══════════════════════════════════════════════════════
    // 7. GUARDAR OBSERVACIONES
    // ═══════════════════════════════════════════════════════
    fun saveObservations(observationText: String) {
        viewModelScope.launch {
            try {
                val state = _state.value

                state.currentSectionObservations.forEach { obsUI ->
                    saveObservationResponseUseCase.invoke(
                        assignedServiceId = serviceId,
                        sectionIndex = state.currentSectionIndex,
                        observationIndex = state.currentSectionObservations.indexOf(obsUI),
                        observationDescription = "", //obsUI.observation.description,
                        response = observationText
                    )

/*
                    checklistRepository.saveObservationResponse(
                        assignedServiceId = serviceId,
                        sectionIndex = state.currentSectionIndex,
                        observationIndex = state.currentSectionObservations.indexOf(obsUI),
                        observationDescription = "", //obsUI.observation.description,
                        response = observationText
                    )
 */
                }

                println("✅ Observaciones guardadas: ${observationText.take(30)}...")
            } catch (e: Exception) {
                println("❌ Error: ${e.message}")
            }
        }
    }

    // ═══════════════════════════════════════════════════════
    // 8. ACTUALIZAR OBSERVACIONES (en tiempo real)
    // ═══════════════════════════════════════════════════════
    fun updateObservations(text: String) {
        if (text.length <= 300) {
            _observations.value = text
        }
    }

    // ═══════════════════════════════════════════════════════
    // 9. IR A SIGUIENTE SECCIÓN
    // ═══════════════════════════════════════════════════════
    fun nextSection() {
        viewModelScope.launch {
            try {
                val state = _state.value
                saveObservations(_observations.value)
                val nextIndex = state.currentSectionIndex + 1

                if (nextIndex < state.totalSections) {
                    template?.let { tmpl ->
                        val nextSectionUI = loadSectionUIModel(
                            template = tmpl,
                            sectionIndex = nextIndex,
                            serviceId = serviceId
                        )

                        // 🆕 RECALCULA PROGRESO DE LA NUEVA SECCIÓN
                        val newSectionTotalActivities = nextSectionUI.activities.size
                        val newSectionCompletedActivities = nextSectionUI.activities.count {
                            it.progress?.completed == true
                        }
                        val newSectionProgressPercentage = if (newSectionTotalActivities > 0) {
                            (newSectionCompletedActivities * 100) / newSectionTotalActivities
                        } else {
                            0
                        }

                        _state.value = state.copy(
                            currentSectionIndex = nextIndex,
                            currentSectionName = nextSectionUI.section.name,
                            currentSectionActivities = nextSectionUI.activities,

                            // 🆕 ACTUALIZA CON NUEVOS VALORES
                            sectionTotalActivities = newSectionTotalActivities,
                            sectionCompletedActivities = newSectionCompletedActivities,
                            sectionProgressPercentage = newSectionProgressPercentage,

                            canContinue = false,
                            observations = ""
                        )

                        _observations.value = ""

                        // 🆕 LOG PARA VERIFICAR
                        println("✅ Siguiente sección: ${nextSectionUI.section.name}")
                        println("   Progreso sección: $newSectionCompletedActivities/$newSectionTotalActivities ($newSectionProgressPercentage%)")
                    }
                } else {
                    _state.value = state.copy(
                        allSectionsComplete = true,
                        canContinue = true
                    )
                    println("✅ TODAS LAS SECCIONES COMPLETADAS")
                }
            } catch (e: Exception) {
                println("❌ Error: ${e.message}")
            }
        }
    }

    // ═══════════════════════════════════════════════════════
    // 10. FINALIZAR CHECKLIST
    // ═══════════════════════════════════════════════════════
    fun onContinueClicked() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                saveObservations(_observations.value)
                delay(1000)
                _isLoading.value = false
                println("✅ Checklist finalizado y guardado")
            } catch (e: Exception) {
                _isLoading.value = false
                println("❌ Error: ${e.message}")
            }
        }
    }

}
