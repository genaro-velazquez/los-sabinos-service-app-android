package com.lossabinos.serviceapp.screens.checklist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lossabinos.serviceapp.ui.components.organisms.ActivityTaskItem
import com.lossabinos.serviceapp.ui.components.templates.ChecklistProgressTemplate
import com.lossabinos.serviceapp.viewmodel.ChecklistViewModel
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.lossabinos.serviceapp.navigation.NavigationEvent
import com.lossabinos.serviceapp.navigation.Routes
import com.lossabinos.serviceapp.screens.camera.CameraScreen
import com.lossabinos.serviceapp.screens.dialogs.PhotoViewerDialog
import com.lossabinos.serviceapp.ui.components.organisms.ConfirmationDialog
import com.lossabinos.serviceapp.ui.components.organisms.ExtraCostModal
import com.lossabinos.serviceapp.viewmodel.ExtraCostViewModel
import kotlinx.coroutines.isActive
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistProgressScreen(
    serviceId: String,
    checklistTemplateJson: String,
    onBackClick: () -> Unit,
    navController: NavController,
    viewModel: ChecklistViewModel = hiltViewModel(),
    extraCostViewModel: ExtraCostViewModel = hiltViewModel()
) {
    // 🆕 AGREGAR AQUÍ - Al inicio, antes de todo
    println("🎯 Abriendo ChecklistProgressScreen")
    println("   - serviceId: $serviceId")
    println("   - checklistTemplateJson: ${checklistTemplateJson.take(50)}...")

    // Picture Dialog
    var showPhotoViewer by remember { mutableStateOf(false) }
    var selectedPhotoIndex by remember { mutableStateOf(0) }
    var allPhotoPaths by remember { mutableStateOf<List<String>>(emptyList()) }

    // 1️⃣ ESTADOS
    val uiState         = viewModel.state.collectAsStateWithLifecycle().value
    val observations    = viewModel.observations.collectAsStateWithLifecycle().value
    val isLoading       = viewModel.isLoading.collectAsStateWithLifecycle().value
    val showSignDialog = viewModel.showSignDialog.collectAsStateWithLifecycle().value
    val navigationEvent = viewModel.navigationEvent.collectAsStateWithLifecycle().value
    val errorMessage = viewModel.errorMessage.collectAsStateWithLifecycle().value
    // ═══════════════════════════════════════════════════════
    // 🆕 EXTRA COSTS STATES
    // ═══════════════════════════════════════════════════════
// ✅ NUEVOS - desde ExtraCostViewModel
    val extraCosts = extraCostViewModel.extraCosts.collectAsStateWithLifecycle().value
    val showExtraCostModal = extraCostViewModel.showExtraCostModal.collectAsStateWithLifecycle().value
    val currentExtraCostForm = extraCostViewModel.currentExtraCostForm.collectAsStateWithLifecycle().value
    val extraCostFormErrors = extraCostViewModel.extraCostFormErrors.collectAsStateWithLifecycle().value
    val isExtraCostLoading = extraCostViewModel.isExtraCostLoading.collectAsStateWithLifecycle().value
    val showDeleteConfirmation = extraCostViewModel.showDeleteConfirmation.collectAsStateWithLifecycle().value
    val extraCostToDelete = extraCostViewModel.extraCostToDelete.collectAsStateWithLifecycle().value

    println("📊 Extra Costs: ${extraCosts.size}")
    println("💰 Total: ${viewModel.getTotalExtraCosts()}")

    // 2️⃣ LOGS
    println("📱 ChecklistProgressScreen recompone")
    println("   - currentSectionIndex: ${uiState.currentSectionIndex}")
    println("   - totalSections: ${uiState.totalSections}")
    println("   - currentActivities: ${uiState.currentSectionActivities.size}")
    println("   - isLoading: $isLoading")

    // ← ALERTA DE ERROR (agregar AQUÍ)
    if (!errorMessage.isNullOrEmpty()) {
        AlertDialog(
            onDismissRequest = { viewModel.onErrorAlertDismissed() },
            title = {
                Text(
                    "❌ Error en la Sincronización",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = errorMessage ?: "",
                    fontSize = 16.sp,
                    color = Color.Black
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.clearError() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD32F2F)
                    )
                ) {
                    Text("Entendido", color = Color.White)
                }
            }
        )
    }

    // ← OBSERVAR EVENTO DE NAVEGACIÓN
    LaunchedEffect(navigationEvent) {
        when (navigationEvent) {
            is NavigationEvent.NavigateToHome -> {
                println("🔄 [SCREEN] Navegando a Home...")
                // ← USAR popBackStack CON LA RUTA ESPECÍFICA
                navController.popBackStack(
                    route = Routes.HOME,
                    inclusive = false  // No eliminar Home del stack
                )
            }
            else -> {}
        }
    }

    // ← MODAL DE FIRMA
    if (showSignDialog) {
        ConfirmationDialog(
            title = "Confirmación de servicio\n",
            content = "Estás a punto de firmar y confirmar las actividades realizadas en este servicio.\n" +
                    "Al continuar, la información registrada será enviada para su validación y el servicio quedará marcado como completado.\n" +
                    "Asegúrate de que todos los datos sean correctos antes de continuar.\n",
            primaryButtonText = "Firmar",
            secondaryButtonText = "Cancelar",
            onPrimaryClick = {
                println("✍️ [SCREEN] Confirmando firma")
                val completedIndices = uiState.currentSectionActivities
                    .mapIndexed { index, activity ->
                        if (activity.progress?.completed == true) index else null
                    }
                    .filterNotNull()

                viewModel.onConfirmSign()
            },
            onSecondaryClick = {
                println("🚫 [SCREEN] Cancelar firma")
                viewModel.onCancelSign()
            },
            onDismiss = {
                viewModel.onCancelSign()
            }
        )
    }

    // 3️⃣ VARIABLES
    val isLastSection = uiState.currentSectionIndex == uiState.totalSections - 1
    val continueButtonText = if (isLastSection) {
        "Guardar y firmar"
    } else {
        "Continuar"
    }

    // 🆕 Estado para mostrar cámara
    var showCamera = remember { mutableStateOf(false) }
    var currentActivityIndex = remember { mutableStateOf(-1) }
    val localCompletedActivities = remember {
        mutableStateMapOf<Int, Boolean>()
    }

// ✅ CORRECTO - Solo se ejecuta cuando REALMENTE cambian los datos
    LaunchedEffect(
        key1 = uiState.currentSectionIndex,
        key2 = uiState.currentSectionActivities.hashCode()  // Detecta cambios en la lista
    ) {
        println("🔄 LaunchedEffect: Sincronizando actividades...")

        if (uiState.currentSectionActivities.isNotEmpty()) {
            localCompletedActivities.clear()

            uiState.currentSectionActivities.forEachIndexed { index, activityUI ->
                val isCompleted = activityUI.progress?.completed ?: false
                if (isCompleted) {
                    localCompletedActivities[index] = true
                    println("   ✅ Actividad $index = $isCompleted")
                }
            }

            println("📊 Total: ${localCompletedActivities.size}/${uiState.currentSectionActivities.size}")
        }
    }


    // 4️⃣ CALCULAR PROGRESO
    val sectionActivitiesCount = uiState.currentSectionActivities.size
    val localCompletedCount = localCompletedActivities.count { it.value }
    val localProgressPercentage = if (sectionActivitiesCount > 0) {
        (localCompletedCount * 100) / sectionActivitiesCount
    } else {
        0
    }

    // 5️⃣ LAUNCHED EFFECT PARA CARGAR TEMPLATE
    LaunchedEffect(serviceId, checklistTemplateJson) {
        // Solo ejecutar si cambio serviceId o json
        println("🔄 Cargando template...")
        viewModel.loadTemplate(
            checklistTemplateJson = checklistTemplateJson,
            serviceIdParam = serviceId
        )
    }

    // ✅ 5.5️⃣ LAUNCHED EFFECT PARA CARGAR COSTOS EXTRA
    LaunchedEffect(serviceId) {
        println("🔄 Cargando costos extra...")
        extraCostViewModel.loadExtraCosts(serviceId)
    }

    // 🆕 Mostrar cámara si está activada
    if (showCamera.value) {
        CameraScreen(
            onPhotoCaptured = { imagePath ->
                println("📸 Foto capturada en ChecklistProgressScreen: $imagePath")
                println("   Para actividad: ${currentActivityIndex.value}")
                println("   ViewModel: $viewModel")  // 🆕 VERIFICAR viewModel
                println("   ViewScope activo: ${viewModel.viewModelScope.isActive}")  // 🆕 VERIFICAR scope

                // Guardar en Room
                viewModel.addPhotoToActivity(
                    activityIndex = currentActivityIndex.value,
                    photoUri = imagePath
                )

                println("✅ addPhotoToActivity fue LLAMADO")  // 🆕 Log DESPUÉS de llamar

                // Cerrar cámara
                showCamera.value = false
                currentActivityIndex.value = -1
            },
            onBackClick = {
                showCamera.value = false
                currentActivityIndex.value = -1
            }
        )
    } else {

        // ═══════════════════════════════════════════════════════
        // 🆕 DELETE EXTRA COST CONFIRMATION DIALOG
        // ═══════════════════════════════════════════════════════
        if (showDeleteConfirmation && extraCostToDelete != null) {
            ConfirmationDialog(
                title = "Eliminar costo extra",
                content = "¿Estás seguro de que deseas eliminar este costo extra?\n\n" +
                        "${extraCostToDelete.category.icon} ${extraCostToDelete.description}\n\n" +
                        "Esta acción no se puede deshacer.",
                primaryButtonText = "Eliminar",
                secondaryButtonText = "Cancelar",
                onPrimaryClick = {
                    println("✅ [DELETE] Confirming deletion")
                    extraCostViewModel.confirmDeleteExtraCost()
                },
                onSecondaryClick = {
                    println("🚫 [DELETE] Canceling deletion")
                    extraCostViewModel.closeDeleteConfirmation()
                },
                onDismiss = {
                    extraCostViewModel.closeDeleteConfirmation()
                }
            )
        }

        // ═══════════════════════════════════════════════════════
        // 🆕 EXTRA COST MODAL
        // ═══════════════════════════════════════════════════════
        ExtraCostModal(
            isVisible = showExtraCostModal,
            formData = currentExtraCostForm,
            onQuantityChange = { extraCostViewModel.updateExtraCostQuantity(it) },
            onCategoryChange = { extraCostViewModel.updateExtraCostCategory(it) },
            onDescriptionChange = { extraCostViewModel.updateExtraCostDescription(it) },
            onNotesChange = { extraCostViewModel.updateExtraCostNotes(it) },
            onSaveClick = { extraCostViewModel.saveExtraCost(serviceId) },  // ✅ Pasar serviceId
            onCancelClick = { extraCostViewModel.closeExtraCostModal() },
            errors = extraCostFormErrors,
            isEditMode = extraCostViewModel.editingExtraCostId.collectAsStateWithLifecycle().value != null,
            isLoading = isExtraCostLoading
        )

        // Pantalla normal de checklist
        ChecklistProgressTemplate(
            serviceName = uiState.currentSectionName,
            templateName = uiState.templateName,
            currentProgress = uiState.currentSectionIndex + 1,
            totalTasks = uiState.totalActivities,
            progressPercentage = localProgressPercentage,//uiState.sectionProgressPercentage,
            tasks = uiState.currentSectionActivities.mapIndexed { index, activityUI ->
                // 🆕 USAR estado local para checkboxes
                val isLocalCompleted = localCompletedActivities[index]
                    ?: activityUI.progress?.completed ?: false

                ActivityTaskItem(
                    id = "activity_$index",
                    description = activityUI.activity.description,
                    completed = isLocalCompleted, /*activityUI.progress?.completed ?: false,*/
                    requiresEvidence = activityUI.activity.requiresEvidence,
                    hasPhoto = activityUI.evidence.isNotEmpty(),
                    photoUri = activityUI.evidence.firstOrNull()?.filePath?.let {
                        "file://$it"
                    },
                    evidenceId = activityUI.evidence.firstOrNull()?.id ?: 0L
                )
            },
            observations = uiState.currentSectionObservations,  // ← CAMBIAR
            observationResponses = uiState.observationResponses,  // ← AGREGAR
            metadata = uiState.currentSectionMetadata,
            onObservationChange = { observationId, value ->  // ← CAMBIAR
                viewModel.updateObservationResponse(observationId, value)
            },
            onTaskCheckedChange = { taskId, completed ->
                // 🆕 CAMBIO: Solo actualizar estado local, NO guardar
                val index = taskId.removePrefix("activity_").toIntOrNull() ?: return@ChecklistProgressTemplate
                localCompletedActivities[index] = completed

                println("✏️ Checkbox marcado: $index = $completed (NO guardado aún)")
            },
            onCameraClick = { taskId ->
                println("📷 Abriendo cámara para $taskId")
                val index = taskId.removePrefix("activity_").toIntOrNull() ?: return@ChecklistProgressTemplate
                currentActivityIndex.value = index
                showCamera.value = true
            },
            onAddPhoto = { taskId ->
                println("📷 Abriendo cámara para $taskId")
                val index = taskId.removePrefix("activity_").toIntOrNull() ?: return@ChecklistProgressTemplate
                currentActivityIndex.value = index
                showCamera.value = true
            },
            onRemovePhoto = { evidenceId ->  // 🆕 NUEVO CALLBACK
                val index = uiState.currentSectionActivities.indexOfFirst {
                    it.evidence.any { ev -> ev.id == evidenceId }
                }
                if (index >= 0) {
                    viewModel.deleteActivityEvidence(evidenceId =  evidenceId)
                }
            },
            onPhotoClick = { photoPath ->
                println("📸 Click en foto: $photoPath")

                // ✅ LIMPIAR: Remover file:// si existe
                val cleanPhotoPath = if (photoPath.startsWith("file://")) {
                    photoPath.removePrefix("file://")
                } else {
                    photoPath
                }

                val photosOfActivity = uiState.currentSectionActivities
                    .flatMap { activity -> activity.evidence }
                    .map { it.filePath }

                println("📋 Total fotos disponibles: ${photosOfActivity.size}")
                println("🔍 Buscando (limpia): $cleanPhotoPath")

                val index = photosOfActivity.indexOf(cleanPhotoPath)
                println("📌 Índice encontrado: $index")

                if (index >= 0 && photosOfActivity.isNotEmpty()) {
                    selectedPhotoIndex = index
                    allPhotoPaths = photosOfActivity
                    showPhotoViewer = true
                    println("✅ Visor abierto")
                } else {
                    println("❌ No coincide - revisa las rutas")
                }
            },
            continueButtonText = continueButtonText,
            onContinueClick = {
                if (isLastSection) {
                    // Última sección: PRIMERO guardar, LUEGO mostrar modal
                    println("💾 [SCREEN] Última sección - Guardando antes de firmar...")
                    val completedIndices = localCompletedActivities
                        .filter { it.value }
                        .keys.toList()
                    // Guardar actividades de esta sección
                    viewModel.saveAndNavigateToNextSection(completedIndices)
                    // Luego mostrar modal de firma (después de guardar)
                    viewModel.onSignChecklistClicked()
                } else {
                    // Secciones normales: Avanzar
                    viewModel.saveAndNavigateToNextSection(
                        completedIndices = localCompletedActivities.filter { it.value }.keys.toList()
                    )
                }
            },
            isLoading = isLoading,
            onBackClick = onBackClick,
            extraCosts = extraCosts,
            onAddExtraCostClick = { extraCostViewModel.openAddExtraCostModal() },
            onEditExtraCostClick = { extraCostViewModel.openEditExtraCostModal(it) },
            onDeleteExtraCostClick = { extraCostViewModel.showDeleteConfirmation(it) }
            )

        // Mostrar visor al final
        if (showPhotoViewer && allPhotoPaths.isNotEmpty()) {
            PhotoViewerDialog(
                photoPaths = allPhotoPaths,
                initialIndex = selectedPhotoIndex,
                onDismiss = { showPhotoViewer = false },
                showDeleteButton = true
            )
        }
    }
}