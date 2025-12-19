package com.lossabinos.serviceapp.ui.components.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.components.organisms.ActivitiesListOrganism
import com.lossabinos.serviceapp.ui.components.organisms.ActivityTaskItem
import com.lossabinos.serviceapp.ui.components.organisms.ChecklistProgressHeaderOrganism
import com.lossabinos.serviceapp.ui.components.organisms.ContinueActionOrganism
import com.lossabinos.serviceapp.ui.components.organisms.ObservationsOrganism

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistProgressTemplate(
    serviceName: String,
    templateName:String,
    currentProgress: Int,
    totalTasks: Int,
    progressPercentage: Int,
    tasks: List<ActivityTaskItem> = emptyList(),
    observations: String = "",
    onObservationsChange: (String) -> Unit = {},
    onTaskCheckedChange: (String, Boolean) -> Unit = { _, _ -> },
    onCameraClick: (String) -> Unit = {},
    onAddPhoto: (String) -> Unit = {},
    onContinueClick: () -> Unit = {},
    isLoading: Boolean = false,
    onBackClick: () -> Unit = {}
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
                .padding(paddingValues)  // 🆕 AGREGA ESTO - Respeta el TopAppBar
                .verticalScroll(rememberScrollState())
        ) {
            // ═════════════════════════════════════
            // SECCIÓN 1: Header Progress
            // ═════════════════════════════════════
            ChecklistProgressHeaderOrganism(
                serviceName = serviceName,
                templateName = templateName,
                currentProgress = currentProgress,
                totalTasks = totalTasks,
                progressPercentage = progressPercentage,
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(1.dp))

            // ═════════════════════════════════════
            // SECCIÓN 2: Activities List ✨ NUEVO
            // ═════════════════════════════════════
            ActivitiesListOrganism(
                tasks = tasks,
                onTaskCheckedChange = onTaskCheckedChange,
                onCameraClick = onCameraClick,
                onAddPhoto = onAddPhoto,
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ═════════════════════════════════════════
            // SECCIÓN 3: Observations ✨ NUEVO
            // ═════════════════════════════════════════
            ObservationsOrganism(
                observations = observations,
                onObservationsChange = onObservationsChange,
                maxCharacters = 300
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ═════════════════════════════════════════
            // SECCIÓN 4: Continue Button ✨ NUEVO
            // ═════════════════════════════════════════
            ContinueActionOrganism(
                onClick = onContinueClick,
                enabled = tasks.isNotEmpty(),
                isLoading = isLoading
            )

            Spacer(modifier = Modifier.height(32.dp))

        }
    }
}
