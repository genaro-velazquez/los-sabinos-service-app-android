package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ChecklistTaskCardMolecule(
    taskId: String,
    description: String,
    completed: Boolean,
    requiresEvidence: Boolean,
    hasPhoto: Boolean = false,
    photoUri: String? = null,
    evidenceId: Long = 0L,
    onCheckedChange: (String, Boolean) -> Unit,
    onCameraClick: (String) -> Unit,
    onAddPhoto: (String) -> Unit,
    onRemovePhoto: (Long) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(
            width = 1.dp,
            color = if (requiresEvidence && !completed)
                Color(0xFFFFB74D)  // Amarillo si requiere evidencia
            else
                Color(0xFFE0E0E0)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header: Checkbox + Description + Camera
            TaskHeaderMolecule(
                description = description,
                completed = completed,
                requiresEvidence = requiresEvidence,
                onCheckedChange = { newValue ->
                    onCheckedChange(taskId, newValue)
                },
                onCameraClick = {
                    onCameraClick(taskId)
                }
            )

            // Si requiere evidencia y no está completado: mostrar upload
            if (requiresEvidence) {
                EvidenceUploadMolecule(
                    hasPhoto = hasPhoto,
                    photoUri = photoUri,
                    onAddPhoto = {
                        onAddPhoto(taskId)
                    },
                    onRemovePhoto = {
                        onRemovePhoto(evidenceId)
                        //val index = taskId.removePrefix("activity_").toIntOrNull() ?: return@EvidenceUploadMolecule
                        // Obtener el ID de la evidencia para eliminar
                        // Necesitas pasar esto desde el ViewModel
                        // viewModel.deleteActivityEvidence(index, evidenceId)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChecklistTaskCardMoleculePreview(){
    MaterialTheme{
        ChecklistTaskCardMolecule(
            taskId = "task id",
            description = "Description",
            completed = true,
            requiresEvidence = true,
            onCheckedChange = {_, _ ->},
            onCameraClick = {},
            onAddPhoto = {}
        )
    }
}