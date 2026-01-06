package com.lossabinos.serviceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lossabinos.data.local.database.entities.ActivityEvidenceEntity
import com.lossabinos.data.local.database.entities.ActivityProgressEntity
import com.lossabinos.data.local.mappers.toEntity
//import com.lossabinos.data.repositories.local.ChecklistRepository
import com.lossabinos.domain.entities.ActivityEvidence
import com.lossabinos.domain.usecases.checklist.CompleteActivityUseCase
import com.lossabinos.domain.usecases.checklist.DeleteActivityEvidenceByIdUseCase
import com.lossabinos.domain.usecases.checklist.GetActivitiesProgressForSectionUseCase
import com.lossabinos.domain.usecases.checklist.GetEvidenceForActivityUseCase
import com.lossabinos.domain.usecases.checklist.GetObservationResponsesForSectionUseCase
import com.lossabinos.domain.usecases.checklist.GetTotalCompletedActivitiesUseCase
import com.lossabinos.domain.usecases.checklist.SaveActivityEvidenceUseCase
import com.lossabinos.domain.usecases.checklist.SaveObservationResponseUseCase
import com.lossabinos.domain.usecases.checklist.SaveServiceFieldValueUseCase
import com.lossabinos.domain.usecases.checklist.SaveServiceFieldValuesUseCase
import com.lossabinos.domain.usecases.checklist.SaveServiceProgressUseCase
import com.lossabinos.domain.valueobjects.ServiceStatus
import com.lossabinos.domain.valueobjects.SyncStatus
import com.lossabinos.domain.valueobjects.Template
import com.lossabinos.serviceapp.models.ActivityModel
import com.lossabinos.serviceapp.models.ActivityUIModel
import com.lossabinos.serviceapp.models.ObservationUIModel
import com.lossabinos.serviceapp.models.SectionModel
import com.lossabinos.serviceapp.models.SectionUIModel
import com.lossabinos.serviceapp.models.VehicleRegistrationFieldUIModel
import com.lossabinos.serviceapp.models.toDomain
import com.lossabinos.serviceapp.navigation.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.io.File
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
    private val completeActivityUseCase: CompleteActivityUseCase,
    private val getTotalCompletedActivitiesUseCase: GetTotalCompletedActivitiesUseCase,
    private val getObservationResponsesForSectionUseCase: GetObservationResponsesForSectionUseCase,
    private val getActivitiesProgressForSectionUseCase:  GetActivitiesProgressForSectionUseCase,
    private val getEvidenceForActivityUseCase: GetEvidenceForActivityUseCase,
    private val saveActivityEvidenceUseCase: SaveActivityEvidenceUseCase,
    private val deleteActivityEvidenceByIdUseCase: DeleteActivityEvidenceByIdUseCase,
    private val saveObservationResponseUseCase: SaveObservationResponseUseCase,
    private val saveServiceProgressUseCase: SaveServiceProgressUseCase
) : ViewModel() {

    private val _showSignDialog = MutableStateFlow(false)
    val showSignDialog: StateFlow<Boolean> = _showSignDialog.asStateFlow()

    private val _navigationEvent = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvent: StateFlow<NavigationEvent?> = _navigationEvent.asStateFlow()

    // ← AGREGAR ESTOS MÉTODOS
    fun onSignChecklistClicked() {
        _showSignDialog.value = true
        println("📋 [CHECKLIST] Abriendo diálogo de firma")
    }

    fun onConfirmSign() {
        _showSignDialog.value = false
        viewModelScope.launch {
            try {
                println("✍️ [CHECKLIST] Confirmando firma...")
                _navigationEvent.value = NavigationEvent.NavigateToHome
            } catch (e: Exception) {
                println("❌ Error: ${e.message}")
            }
        }
    }

    fun onCancelSign() {
        _showSignDialog.value = false
        println("🚫 [CHECKLIST] Firma cancelada")
    }


    private val _state = MutableStateFlow(ChecklistUIState())
    val state: StateFlow<ChecklistUIState> = _state.asStateFlow()

    private val _observations = MutableStateFlow("")
    val observations: StateFlow<String> = _observations.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var template: Template? = null
    private var serviceId:String = ""
    private var totalActivitiesInService: Int = 0

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

                println("📋 Cargando template para servicio: $serviceIdParam")

                // 🆕 LOG: Verificar datos en Room
                val activitiesFromRoom = getActivitiesProgressForSectionUseCase.invoke(
                    assignedServiceId = serviceIdParam,
                    sectionIndex = 0
                )

                println("📊 Actividades en Room (sección 0): ${activitiesFromRoom.size}")
                activitiesFromRoom.forEach { activity ->
                    println("   - ${activity.activityDescription}: ${activity.completed}")
                }

                val evidencesFromRoom = getEvidenceForActivityUseCase.invoke(
                    activityProgressId = activitiesFromRoom.firstOrNull()?.id ?: 0L
                )


                println("📸 Evidencias en Room: ${evidencesFromRoom.size}")
                evidencesFromRoom.forEach { evidence ->
                    println("   - ${evidence.filePath}")
                }

                // ✨ PASO 1: Deserializar JSON → Domain
                template = Json.decodeFromString(checklistTemplateJson)

                template?.let { tmpl ->
                    println("✅ Template deserializado: ${tmpl.sections.size} secciones")

                    // ✅ CALCULAR TOTAL DE TODAS LAS ACTIVIDADES DEL SERVICIO
                    totalActivitiesInService = tmpl.sections.sumOf { section ->
                        section.activities.size
                    }

                    // ✨ PASO 2: Cargar progreso previo de Room
                    //val totalCompleted = checklistRepository.getTotalCompletedActivities(serviceId)
                    val totalCompleted = getTotalCompletedActivitiesUseCase.invoke(assignedServiceId = serviceId)
                    println("✅ Actividades completadas previas: $totalCompleted")

                    // ✨ PASO 3: Encontrar sección actual
                    val currentSectionIndex = 0 //findFirstIncompleteSection(tmpl, serviceId)
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

    // ════════════════════════════════════════════════════════════════════════════════════════
    // 5. MARCAR ACTIVIDAD COMPLETADA (NO SE USA, SE SUSTUTIYO POR saveAllCompletedActivities)
    // ═════════════════════════════════════════════════════════════════════════════════════════
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
        println("🔴 addPhotoToActivity: Iniciando...")
        viewModelScope.launch {
            println("🟡 DENTRO del viewModelScope.launch")

            try {
                println("📸 addPhotoToActivity llamado")
                println("   Actividad: $activityIndex")
                println("   Foto: $photoUri")

                val state = _state.value
                val activity = state.currentSectionActivities[activityIndex]

                println("🔍 Activity obtenida: ${activity.activity.description}")
                println("   ¿Tiene progress? ${activity.progress != null}")
                println("   ¿ID del progress? ${activity.progress?.id}")

                // 🆕 CRÍTICO: Si no tiene progress, completar la actividad PRIMERO
                val progressId = if (activity.progress == null) {
                    println("⚠️ Actividad no tiene progress, completando primero...")
                    completeActivityUseCase.invoke(
                        assignedServiceId = serviceId,
                        sectionIndex = state.currentSectionIndex,
                        activityIndex = activityIndex,
                        description = activity.activity.description,
                        requiresEvidence = activity.activity.requiresEvidence
                    )
                } else {
                    activity.progress.id
                }

                println("✅ Progress ID obtenido: $progressId")

                // Guardar foto en Room
                println("💾 Guardando foto en Room...")
                saveActivityEvidenceUseCase.invoke(
                    id = 0,
                    activityProgressId = progressId,
                    filePath = photoUri,
                    fileType = "image"
                )

                println("✅ Foto guardada en Room")

                // 🆕 ESPERAR a que Room guarde
                delay(500)

                val file = File(photoUri)
                println("📁 VERIFICACIÓN DE ARCHIVO:")
                println("   Ruta: $photoUri")
                println("   Existe: ${file.exists()}")
                println("   Legible: ${file.canRead()}")
                println("   Tamaño: ${file.length()} bytes")

                // 🆕 Recargar evidencias
                println("🔄 Recargando evidencias...")
                val evidencesFromRoom = getEvidenceForActivityUseCase.invoke(
                    activityProgressId = progressId
                )

                println("✅ Evidencias en Room: ${evidencesFromRoom.size}")

                // 🆕 Actualizar estado
                val updatedActivities = state.currentSectionActivities.toMutableList()

                val newProgress = if (activity.progress == null) {
                    ActivityProgressEntity(
                        id = progressId,
                        assignedServiceId = serviceId,
                        sectionIndex = state.currentSectionIndex,
                        activityIndex = activityIndex,
                        activityDescription = activity.activity.description,
                        requiresEvidence = activity.activity.requiresEvidence,
                        completed = true
                    )
                } else {
                    activity.progress
                }

                val updatedActivity = activity.copy(
                    progress = newProgress,
                    evidence = evidencesFromRoom.map { it.toEntity() }
                )

                updatedActivities[activityIndex] = updatedActivity

                println("🎨 Actualizando UI con ${updatedActivity.evidence.size} foto(s)")

                _state.value = state.copy(
                    currentSectionActivities = updatedActivities
                )

                println("✅ UI actualizada completamente")

            } catch (e: Exception) {
                println("❌ Error en viewModelScope.launch: ${e.message}")
                println("   Stack: ${e.stackTrace.take(5).joinToString("\n")}")
                e.printStackTrace()
            }
        }
    }

    fun deleteActivityEvidence(evidenceId: Long) {
        viewModelScope.launch {
            try {
                deleteActivityEvidenceByIdUseCase(evidenceId)

                _state.update { state ->
                    state.copy(
                        currentSectionActivities = state.currentSectionActivities.map { activity ->
                            activity.copy(
                                evidence = activity.evidence.filterNot { it.id == evidenceId }
                            )
                        }
                    )
                }
                println("✅ UI actualizada sin foto")
            }
            catch (e: java.lang.Exception){
                println("❌ Error eliminando evidencia: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    /*
        fun deleteActivityEvidence(activityIndex: Int, evidenceId: Long) {
            viewModelScope.launch {
                try {
                    val state = _state.value
                    val activity = state.currentSectionActivities[activityIndex]

                    println("🗑️ Eliminando evidencia: $evidenceId")

                    // Eliminar del archivo físico también si quieres -> Se paso a  RepositoryImp
                    //¿Por qué?
                    //El ViewModel no debería tocar el filesystem
                    //Eso es Data layer
                    /*
                    activity.evidence.find { it.id == evidenceId }?.let { evidence ->
                        File(evidence.filePath).delete()
                        println("🗑️ Archivo eliminado: ${evidence.filePath}")
                    }
                    */

                    // Eliminar de Room
                    deleteActivityEvidenceByIdUseCase.invoke(evidenceId = evidenceId)
                    // O usar un repository si lo tienes

                    println("✅ Evidencia eliminada")

                    // Recargar evidencias
                    val updatedActivities = state.currentSectionActivities.toMutableList()
                    val updatedActivity = activity.copy(
                        evidence = activity.evidence.filter { it.id != evidenceId }
                    )
                    updatedActivities[activityIndex] = updatedActivity

                    _state.value = state.copy(
                        currentSectionActivities = updatedActivities
                    )

                    println("✅ UI actualizada sin foto")

                } catch (e: Exception) {
                    println("❌ Error eliminando evidencia: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
     */

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
        println("🟢 addPhotoToActivity: Método finalizado (launch asincrónico)")
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

    // ═══════════════════════════════════════════════════════
    // 🆕 GUARDAR TODAS LAS ACTIVIDADES COMPLETADAS (al click "Continuar")
    // ═══════════════════════════════════════════════════════
    fun saveAllCompletedActivities(completedIndices: List<Int>) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val state = _state.value

                println("💾 Guardando ${completedIndices.size} actividades completadas...")

                // 1️⃣ Guardar cada actividad en Room
                completedIndices.forEach { activityIndex ->
                    val activity = state.currentSectionActivities.getOrNull(activityIndex)

                    if (activity != null && activity.progress == null) {
                        println("   - Guardando actividad $activityIndex")

                        completeActivityUseCase.invoke(
                            assignedServiceId = serviceId,
                            sectionIndex = state.currentSectionIndex,
                            activityIndex = activityIndex,
                            description = activity.activity.description,
                            requiresEvidence = activity.activity.requiresEvidence
                        )
                    }
                }

                println("✅ Todas las actividades guardadas en Room")

                delay(500)

                // 2️⃣ SOLO recalcular totales (NO cargar siguiente sección)
                val totalCompleted = getTotalCompletedActivitiesUseCase.invoke(
                    assignedServiceId = serviceId
                )
                val newPercentage = (totalCompleted * 100) / state.totalActivities

                // 3️⃣ Recalcular de la sección actual
                val currentSectionActivities = state.currentSectionActivities.map { activityUI ->
                    if (completedIndices.contains(state.currentSectionActivities.indexOf(activityUI))) {
                        activityUI.copy(
                            progress = ActivityProgressEntity(
                                id = 0, // Temporal
                                assignedServiceId = serviceId,
                                sectionIndex = state.currentSectionIndex,
                                activityIndex = state.currentSectionActivities.indexOf(activityUI),
                                activityDescription = activityUI.activity.description,
                                requiresEvidence = activityUI.activity.requiresEvidence,
                                completed = true
                            )
                        )
                    } else {
                        activityUI
                    }
                }

                val newSectionCompletedActivities = currentSectionActivities.count {
                    it.progress?.completed == true
                }
                val newSectionProgressPercentage = if (state.sectionTotalActivities > 0) {
                    (newSectionCompletedActivities * 100) / state.sectionTotalActivities
                } else {
                    0
                }

                _state.value = state.copy(
                    completedActivities = totalCompleted,
                    progressPercentage = newPercentage,
                    sectionCompletedActivities = newSectionCompletedActivities,
                    sectionProgressPercentage = newSectionProgressPercentage
                )

                println("✅ Estado actualizado:")
                println("   - Completadas: $totalCompleted/${state.totalActivities}")
                println("   - Progreso: $newPercentage%")
                println("   - Sección: $newSectionCompletedActivities/${state.sectionTotalActivities}")

                _isLoading.value = false

            } catch (e: Exception) {
                println("❌ Error guardando actividades: ${e.message}")
                _isLoading.value = false
                e.printStackTrace()
            }
        }
    }

    fun saveAndNavigateToNextSection(completedIndices: List<Int>) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val state = _state.value

                println("💾 Guardando ${completedIndices.size} actividades completadas...")

                // 1️⃣ Guardar actividades
                completedIndices.forEach { activityIndex ->
                    val activity = state.currentSectionActivities.getOrNull(activityIndex)
                    if (activity != null && activity.progress == null) {
                        completeActivityUseCase.invoke(
                            assignedServiceId = serviceId,
                            sectionIndex = state.currentSectionIndex,
                            activityIndex = activityIndex,
                            description = activity.activity.description,
                            requiresEvidence = activity.activity.requiresEvidence,
                            completed = true,
                            completedAt = System.currentTimeMillis().toString()
                        )
                    }
                }

                println("✅ Todas las actividades guardadas")
                delay(500)

                // 2️⃣ Recalcular
                val totalCompleted = getTotalCompletedActivitiesUseCase.invoke(
                    assignedServiceId = serviceId
                )
                val newPercentage =
                    if (totalActivitiesInService > 0)
                        (totalCompleted * 100) / totalActivitiesInService
                    else 0

                val status = when {
                    totalCompleted == totalActivitiesInService -> ServiceStatus.COMPLETED
                    totalCompleted > 0 -> ServiceStatus.IN_PROGRESS
                    else -> ServiceStatus.PENDING
                }

                // 3️⃣ Guardar progreso del servicio
                try {
                    saveServiceProgressUseCase.invoke(
                        assignedServiceId = serviceId,
                        completedActivities = totalCompleted,
                        totalActivities = totalActivitiesInService,
                        completedPercentage = newPercentage,
                        status = status,
                        syncStatus = SyncStatus.PENDING,
                        lastUpdatedAt = System.currentTimeMillis()
                    )
                    println("✅ Progreso del servicio guardado checklistViewModel")
                }
                catch (e: Exception){
                    println("❌ Error guardando progreso: ${e.message}")
                }

                _state.value = state.copy(
                    completedActivities = totalCompleted,
                    progressPercentage = newPercentage
                )

                // 3️⃣ AHORA SÍ, navegar a siguiente sección
                // (Después de guardar, no antes)
                val nextIndex = state.currentSectionIndex + 1

                if (nextIndex < state.totalSections) {
                    template?.let { tmpl ->
                        val nextSectionUI = loadSectionUIModel(
                            template = tmpl,
                            sectionIndex = nextIndex,
                            serviceId = serviceId
                        )

                        try {
                            saveObservations(_observations.value)
                        }
                        catch (e: Exception){
                            println("⚠️ Error guardando observaciones: ${e.message}")
                        }

                        _state.value = state.copy(
                            currentSectionIndex = nextIndex,  // ✅ Navega DESPUÉS de guardar
                            currentSectionName = nextSectionUI.section.name,
                            currentSectionActivities = nextSectionUI.activities,
                            canContinue = false,
                            observations = ""
                        )

                        println("✅ Siguiente sección: ${nextSectionUI.section.name}")
                    }
                } else {
                    _state.value = state.copy(
                        allSectionsComplete = true
                    )
                    println("✅ TODAS LAS SECCIONES COMPLETADAS")
                }

                _isLoading.value = false

            } catch (e: Exception) {
                println("❌ Error: ${e.message}")
                _isLoading.value = false
                e.printStackTrace()
            }
        }
    }

}
