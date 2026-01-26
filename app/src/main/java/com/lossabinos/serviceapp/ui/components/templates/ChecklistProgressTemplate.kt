package com.lossabinos.serviceapp.ui.components.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Note
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Note
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lossabinos.domain.entities.Metadata
import com.lossabinos.domain.entities.Observation
import com.lossabinos.serviceapp.models.MetadataModel
import com.lossabinos.serviceapp.models.ObservationModel
import com.lossabinos.serviceapp.models.ObservationUIModel
import com.lossabinos.serviceapp.models.ui.ExtraCostUIModel
import com.lossabinos.serviceapp.ui.components.organisms.ActivitiesListOrganism
import com.lossabinos.serviceapp.ui.components.organisms.ActivityTaskItem
import com.lossabinos.serviceapp.ui.components.organisms.ChecklistProgressHeaderOrganism
import com.lossabinos.serviceapp.ui.components.organisms.ContinueActionOrganism
import com.lossabinos.serviceapp.ui.components.organisms.ExtraCostSection
import com.lossabinos.serviceapp.ui.components.organisms.MetadataListOrganism
import com.lossabinos.serviceapp.ui.components.organisms.ObservationsOrganism

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistProgressTemplate(
    serviceName: String,
    templateName: String,
    currentProgress: Int,
    totalTasks: Int,
    progressPercentage: Int,
    tasks: List<ActivityTaskItem> = emptyList(),
    observations: List<ObservationModel> = emptyList(),
    observationResponses: Map<String, String> = emptyMap(),
    metadata: List<MetadataModel> = emptyList(),
    onObservationChange: (String, String) -> Unit = { _, _ -> },
    onTaskCheckedChange: (String, Boolean) -> Unit = { _, _ -> },
    onCameraClick: (String) -> Unit = {},
    onAddPhoto: (String) -> Unit = {},
    onRemovePhoto: (Long) -> Unit = {},
    onPhotoClick: (String) -> Unit = {},
    continueButtonText: String = "Continuar",
    onContinueClick: () -> Unit = {},
    isLoading: Boolean = false,
    onBackClick: () -> Unit = {},
    extraCosts: List<ExtraCostUIModel> = emptyList(),
    onAddExtraCostClick: () -> Unit = {},
    onEditExtraCostClick: (ExtraCostUIModel) -> Unit = {},
    onDeleteExtraCostClick: (ExtraCostUIModel) -> Unit = {},
    ) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Progreso") },
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
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            ChecklistProgressHeaderOrganism(
                serviceName = serviceName,
                templateName = templateName,
                currentProgress = currentProgress,
                totalTasks = totalTasks,
                progressPercentage = progressPercentage,
                modifier = Modifier.padding(16.dp)
            )

            //Spacer(modifier = Modifier.fillMaxWidth().height(8.dp).background(color = Color.Red))

            MetadataListOrganism(
                metadata = metadata,
                title = "NOTAS Y REQUISITOS",
                icon = Icons.AutoMirrored.Filled.Notes,
                modifier = Modifier.padding(16.dp)
            )

            //Spacer(modifier = Modifier.fillMaxWidth().height(8.dp).background(color = Color.Red))

            ActivitiesListOrganism(
                tasks = tasks,
                onTaskCheckedChange = onTaskCheckedChange,
                onCameraClick = onCameraClick,
                onAddPhoto = onAddPhoto,
                onRemovePhoto = onRemovePhoto,
                onPhotoClick = onPhotoClick,
                modifier = Modifier.padding(16.dp)
            )

            //Spacer(modifier = Modifier.fillMaxWidth().height(8.dp).background(color = Color.Red))

            ObservationsOrganism(
                observations = observations,
                observationResponses = observationResponses,
                onObservationChange = onObservationChange,
                modifier = Modifier.padding(16.dp)
            )

            //Spacer(modifier = Modifier.height(32.dp).background(color = Color.Red))

            ExtraCostSection(
                extraCosts = extraCosts,
                onAddCostClick = onAddExtraCostClick,
                onEditCostClick = onEditExtraCostClick,
                onDeleteCostClick = onDeleteExtraCostClick,
                modifier = Modifier.padding(16.dp)
            )

            ContinueActionOrganism(
                onClick = onContinueClick,
                enabled = tasks.isNotEmpty(),
                isLoading = isLoading,
                text = continueButtonText
            )

            Spacer(modifier = Modifier.height(32.dp).background(color = Color.Red))
        }
    }
}
