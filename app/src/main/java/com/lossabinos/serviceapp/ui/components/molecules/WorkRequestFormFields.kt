package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.lossabinos.domain.entities.WorkRequestPhoto
import com.lossabinos.serviceapp.events.WorkRequestUiEvent
import com.lossabinos.serviceapp.models.ui.UrgencyUI
import com.lossabinos.serviceapp.models.ui.WorkRequestUIModel
import com.lossabinos.serviceapp.ui.components.atoms.ActionButtonAtom
import com.lossabinos.serviceapp.ui.components.atoms.CheckboxRow
import com.lossabinos.serviceapp.ui.components.atoms.ErrorText
import com.lossabinos.serviceapp.ui.components.atoms.TextInput
import com.lossabinos.serviceapp.ui.components.organisms.UrgencyDropdown
import com.lossabinos.serviceapp.viewmodel.WorkRequestViewModel
import java.io.File

@Composable
fun WorkRequestFormFields(
    formData: WorkRequestUIModel,
    photos: List<WorkRequestPhoto>,
    viewModel: WorkRequestViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        TextInput(
            value = formData.title,
            onValueChange = {
                viewModel.onEvent(WorkRequestUiEvent.OnTitleChange(it))
            },
            placeholder = "Título"
        )

        TextInput(
            value = formData.description,
            onValueChange = {
                viewModel.onEvent(WorkRequestUiEvent.OnDescriptionChange(it))
            },
            placeholder = "Descripción"
        )

        TextInput(
            value = formData.findings,
            onValueChange = {
                viewModel.onEvent(WorkRequestUiEvent.OnFindingsChange(it))
            },
            placeholder = "Hallazgos"
        )

        TextInput(
            value = formData.justification,
            onValueChange = {
                viewModel.onEvent(WorkRequestUiEvent.OnJustificationChange(it))
            },
            placeholder = "Justificación"
        )

        // ✅ NUEVO: Dropdown de urgencia
        UrgencyDropdown(
            selected = formData.urgency,
            onUrgencySelected = {
                viewModel.onEvent(WorkRequestUiEvent.OnUrgencyChange(it))
            }
        )

        ActionButtonAtom(
            text = "Tomar foto (${photos.size}/3)",
            icon = Icons.Default.CameraAlt,
            enabled = photos.size < 3,
            onClick = {
                // aquí solo disparas cámara
            }
        )

        //Mostrar fotos guardadas
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(photos) { photo ->
                Box {
                    Image(
                        painter = rememberAsyncImagePainter(File(photo.localPath)),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    IconButton(
                        onClick = {
                            viewModel.onEvent(
                                WorkRequestUiEvent.OnPhotoDeleted(photo.id)
                            )
                        },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Eliminar"
                        )
                    }
                }
            }
        }

        CheckboxRow(
            checked = formData.requiresCustomerApproval,
            label = "Requiere aprobación del cliente",
            onCheckedChange = {
                viewModel.onEvent(
                    WorkRequestUiEvent.OnRequiresApprovalChange(it)
                )
            }
        )
    }
}


