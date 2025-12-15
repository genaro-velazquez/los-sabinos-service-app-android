package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.components.atoms.CameraButton
import com.lossabinos.serviceapp.ui.components.atoms.CheckboxTask
import com.lossabinos.serviceapp.ui.components.atoms.TaskDescription

@Composable
fun TaskHeaderMolecule(
    description: String,
    completed: Boolean,
    requiresEvidence: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onCameraClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Checkbox + Description
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CheckboxTask(
                checked = completed,
                onCheckedChange = onCheckedChange
            )
            TaskDescription(
                description = description,
                status = if (completed && !requiresEvidence) "Completado" else null
            )
        }

        // Camera Button (solo si requiere evidencia y no está completado)
        if (requiresEvidence && !completed) {
            CameraButton(onClick = onCameraClick)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskHeaderMoleculePreview(){
    MaterialTheme{
        TaskHeaderMolecule(
            description = "Description",
            completed = true,
            requiresEvidence = true,
            onCheckedChange = {},
            onCameraClick = {}
        )
    }
}