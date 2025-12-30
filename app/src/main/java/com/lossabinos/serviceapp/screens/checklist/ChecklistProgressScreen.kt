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
import androidx.lifecycle.viewModelScope
import com.lossabinos.serviceapp.screens.camera.CameraScreen
import com.lossabinos.serviceapp.screens.dialogs.PhotoViewerDialog
import kotlinx.coroutines.isActive


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistProgressScreen(
    serviceId: String,
    checklistTemplateJson: String,
    onBackClick: () -> Unit,
    navController: NavController,
    viewModel: ChecklistViewModel = hiltViewModel()

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

    // 2️⃣ LOGS
    println("📱 ChecklistProgressScreen recompone")
    println("   - currentSectionIndex: ${uiState.currentSectionIndex}")
    println("   - totalSections: ${uiState.totalSections}")
    println("   - currentActivities: ${uiState.currentSectionActivities.size}")
    println("   - isLoading: $isLoading")

    // 3️⃣ VARIABLES
    val isLastSection = uiState.currentSectionIndex == uiState.totalSections - 1
    val continueButtonText = if (isLastSection) {
        "Guardar y enviar"
    } else {
        "Continuar"
    }

    // 🆕 Estado para mostrar cámara
    var showCamera = remember { mutableStateOf(false) }
    var currentActivityIndex = remember { mutableStateOf(-1) }
    val localCompletedActivities = remember {
        mutableStateMapOf<Int, Boolean>()
    }

    /*
    // 🆕 INICIALIZAR CON DATOS DE ROOM (solo se ejecuta cuando cambias de sección)
    LaunchedEffect(uiState.currentSectionIndex, uiState.currentSectionActivities) {
        println("🔄 Inicializando actividades completadas de Room (segunda sesión)...")

        localCompletedActivities.clear()

        uiState.currentSectionActivities.forEachIndexed { index, activityUI ->
            val isCompleted = activityUI.progress?.completed ?: false
            if (isCompleted) {
                localCompletedActivities[index] = true
                println("   ✅ Actividad $index completada (de Room)")
            }
        }

        val totalCompleted = localCompletedActivities.count { it.value }
        println("📊 Total: $totalCompleted/${uiState.currentSectionActivities.size}")
    }
    */

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
    /*
    val localCompletedCount = localCompletedActivities.count { it.value }
    val localProgressPercentage = if (uiState.sectionTotalActivities > 0) {
        (localCompletedCount * 100) / uiState.sectionTotalActivities
    } else {
        0
    }
    */


    // 5️⃣ LAUNCHED EFFECT PARA CARGAR TEMPLATE
    LaunchedEffect(serviceId, checklistTemplateJson) {
        // Solo ejecutar si cambio serviceId o json
        println("🔄 Cargando template...")
        viewModel.loadTemplate(
            checklistTemplateJson = checklistTemplateJson,
            serviceIdParam = serviceId
        )
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
            observations = observations,
            onObservationsChange = { newText ->
                viewModel.updateObservations(text = newText)
            },
            onTaskCheckedChange = { taskId, completed ->
                /*
                val index = taskId.removePrefix("activity_").toIntOrNull() ?: return@ChecklistProgressTemplate
                if (completed) {
                    viewModel.completeActivity(index)
                }
                */
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
                /*
                println("📸 Click en foto: $photoPath")

                // ✅ LIMPIAR: Remover file:// si existe
                val cleanPhotoPath = if (photoPath.startsWith("file://")) {
                    photoPath.removePrefix("file://")
                } else {
                    photoPath
                }

                // Obtener todas las fotos de la actividad actual
                val photosOfActivity = uiState.currentSectionActivities
                    .flatMap { activity -> activity.evidence }
                    .map { it.filePath }

                println("📋 Total fotos disponibles: ${photosOfActivity.size}")
                photosOfActivity.forEachIndexed { idx, path ->
                    println("   [$idx] $path")
                    println("       ¿Coincide? ${path == photoPath}")
                }

                println("📋 Total fotos disponibles: ${photosOfActivity.size}")
                println("🔍 Buscando (limpia): $cleanPhotoPath")

                val index = photosOfActivity.indexOf(cleanPhotoPath)
                println("🔍 Índice encontrado: $index")

                if (index >= 0 && photosOfActivity.isNotEmpty()) {
                    selectedPhotoIndex = index
                    allPhotoPaths = photosOfActivity
                    showPhotoViewer = true
                    println("✅ Abriendo visor con foto en índice: $index")
                } else {
                    println("❌ Foto no encontrada en lista")
                    println("   - Buscando: $photoPath")
                    println("   - En lista: $photosOfActivity")
                }
                */

                /*
                selectedPhotoIndex = photosOfActivity.indexOf(photoPath)
                allPhotoPaths = photosOfActivity
                showPhotoViewer = true
                */
            },
            continueButtonText = continueButtonText,
            onContinueClick = {
                // 🆕 SOLO LLAMAR ESTE MÉTODO
                viewModel.saveAndNavigateToNextSection(
                    completedIndices = localCompletedActivities.filter { it.value }.keys.toList()
                )
                /*
                if (uiState.allSectionsComplete) {
                    viewModel.onContinueClicked()
                } else {
                    viewModel.nextSection()
                }
                */
                // 🆕 AQUÍ: Guardar todos los checkboxes marcados
                /*
                viewModel.saveAllCompletedActivities(
                    completedIndices = localCompletedActivities.filter { it.value }.keys.toList()
                )

                if (uiState.allSectionsComplete) {
                    viewModel.onContinueClicked()
                } else {
                    viewModel.nextSection()
                }
                */
            },
            isLoading = isLoading,
            onBackClick = onBackClick
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
/*
    ChecklistProgressTemplate(
        serviceName = uiState.currentSectionName,
        templateName = uiState.templateName,
        currentProgress = uiState.currentSectionIndex + 1,
        totalTasks = uiState.totalActivities,
        progressPercentage = uiState.sectionProgressPercentage,
        tasks = uiState.currentSectionActivities.mapIndexed { index, activityUI ->
            ActivityTaskItem(
                id = "activity_$index",
                description = activityUI.activity.description,  // ✨ Del Domain
                completed = activityUI.progress?.completed ?: false,  // ✨ De Room
                requiresEvidence = activityUI.activity.requiresEvidence,  // ✨ Del Domain
                hasPhoto = activityUI.evidence.isNotEmpty(),  // ✨ De Room
                photoUri = activityUI.evidence.firstOrNull()?.filePath  // ✨ De Room
            )
        },
        observations = observations,
        onObservationsChange = { newText ->
            viewModel.updateObservations(text = newText)
        },
        onTaskCheckedChange = { taskId, completed ->
            //viewModel.updateTaskProgress(serviceId, taskId, completed)
            val index = taskId.removePrefix("activity_").toIntOrNull() ?: return@ChecklistProgressTemplate
            if (completed) {
                viewModel.completeActivity(index)
            }
        },
        onCameraClick = { taskId ->
            println("📷 Camera para $taskId")
            viewModel.capturePhoto(serviceId, taskId)
        },
        onAddPhoto = { taskId ->
            println("📷 Camera para $taskId")
            viewModel.selectPhoto(serviceId, taskId)
        },
        onContinueClick = {
            //viewModel.onContinueClicked()
            if (uiState.allSectionsComplete) {
                viewModel.onContinueClicked()
            } else {
                viewModel.nextSection()
            }
        },
        isLoading = isLoading,
        onBackClick = onBackClick
    )
 */

}

/*
@Preview(showBackground = true)
@Composable
fun ChecklistProgressScrennPreview(){

    val tasks = listOf(
        ActivityTaskItem(
            id = "task_1",
            description = "Lectura de medidores A/B",
            completed = true,
            requiresEvidence = true,
            hasPhoto = true,
            photoUri = "..."
        ),
        ActivityTaskItem(
            id = "task_2",
            description = "Inspección visual de fugas",
            completed = false,
            requiresEvidence = true,
            hasPhoto = false
        ),
        ActivityTaskItem(
            id = "task_3",
            description = "Verificar estado de válvulas de presión",
            completed = true,
            requiresEvidence = false
        )
    )

    MaterialTheme{
        ChecklistProgressScreen(
            serviceName = "Service Id",
            serviceType = "Service name",
            currentProgress = 60,
            totalTasks = 5,
            progressPercentage = 30,
            serviceId = "3",
            onBackClick = {}
        )
    }
}
*/