package com.lossabinos.serviceapp.ui.components.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lossabinos.serviceapp.ui.components.molecules.ChecklistTaskCardMolecule

data class ActivityTaskItem(
    val id: String,
    val description: String,
    val completed: Boolean,
    val requiresEvidence: Boolean,
    val hasPhoto: Boolean = false,
    val photoUri: String? = null
)

@Composable
fun ActivitiesListOrganism(
    tasks: List<ActivityTaskItem>,
    onTaskCheckedChange: (String, Boolean) -> Unit,
    onCameraClick: (String) -> Unit,
    onAddPhoto: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Título
        Text(
            text = "ACTIVIDADES",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(start = 4.dp)
        )

        // Lista de tasks
        tasks.forEach { task ->
            ChecklistTaskCardMolecule(
                taskId = task.id,
                description = task.description,
                completed = task.completed,
                requiresEvidence = task.requiresEvidence,
                hasPhoto = task.hasPhoto,
                photoUri = task.photoUri,
                onCheckedChange = onTaskCheckedChange,
                onCameraClick = onCameraClick,
                onAddPhoto = onAddPhoto
            )
        }
    }
}