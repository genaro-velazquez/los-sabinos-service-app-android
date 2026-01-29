package com.lossabinos.serviceapp.ui.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lossabinos.serviceapp.models.ui.UrgencyUI
import com.lossabinos.serviceapp.models.ui.WorkRequestUIModel
import com.lossabinos.serviceapp.ui.components.molecules.WorkRequestFormFields


@Composable
fun WorkRequestModal(
    isVisible: Boolean,
    formData: WorkRequestUIModel,/*
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onFindingsChange: (String) -> Unit,
    onJustificationChange: (String) -> Unit,
    onUrgencyChange: (UrgencyUI) -> Unit,
    onRequiresApprovalChange: (Boolean) -> Unit,*/
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    isLoading: Boolean = false
) {
    if (!isVisible) return

    Dialog(
        onDismissRequest = onCancelClick,
        properties = DialogProperties(
            dismissOnBackPress = !isLoading,
            dismissOnClickOutside = !isLoading,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.90f)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Column(modifier = Modifier.fillMaxHeight()) {

                // ═════════════════ HEADER ═════════════════
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainerHigh,
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp
                            )
                        )
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Nuevo Reporte",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = onCancelClick,
                        enabled = !isLoading
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // ═════════════════ CONTENT ═════════════════
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    WorkRequestFormFields(
                        formData = formData,
                        onTitleChange = {},// onTitleChange,
                        onDescriptionChange = {},//onDescriptionChange,
                        onFindingsChange = {},//onFindingsChange,
                        onJustificationChange = {},//onJustificationChange,
                        onUrgencyChange = {},//onUrgencyChange,
                        onRequiresApprovalChange = {},//onRequiresApprovalChange
                    )
                }

                // ═════════════════ FOOTER ═════════════════
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = onCancelClick,
                        enabled = !isLoading,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text("Cancelar")
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = onSaveClick,
                        enabled = formData.isValid() && !isLoading,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            if (isLoading) "Guardando..." else "Guardar"
                        )
                    }
                }
            }
        }
    }
}
