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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lossabinos.serviceapp.ui.components.organisms.ActivityTaskItem
import com.lossabinos.serviceapp.ui.components.templates.ChecklistProgressTemplate
import com.lossabinos.serviceapp.viewmodel.ChecklistViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistProgressScreen(
    serviceId: String,
    checklistTemplateJson: String,
    onBackClick: () -> Unit,
    viewModel: ChecklistViewModel = hiltViewModel()

) {
    val uiState         = viewModel.state.collectAsStateWithLifecycle().value
    val observations    = viewModel.observations.collectAsStateWithLifecycle().value
    val isLoading       = viewModel.isLoading.collectAsStateWithLifecycle().value

    // ✨ CARGAR TEMPLATE AL ABRIR
    LaunchedEffect(Unit) {
        println("📱 ChecklistProgressScreen abierto")
        viewModel.loadTemplate(
            checklistTemplateJson = checklistTemplateJson,
            serviceIdParam = serviceId
        )
    }

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