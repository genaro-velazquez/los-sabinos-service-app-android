package com.lossabinos.serviceapp.ui.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lossabinos.serviceapp.models.ui.ExtraCostCategory
import com.lossabinos.serviceapp.models.ui.ExtraCostFormErrors
import com.lossabinos.serviceapp.models.ui.ExtraCostUIModel
import com.lossabinos.serviceapp.ui.components.molecules.ExtraCostFormFields

// ═══════════════════════════════════════════════════════
// EXTRA COST MODAL ORGANISM
// ═══════════════════════════════════════════════════════
@Composable
fun ExtraCostModal(
    isVisible: Boolean,
    formData: ExtraCostUIModel,
    onQuantityChange: (Double) -> Unit,
    onCategoryChange: (ExtraCostCategory) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    errors: ExtraCostFormErrors = ExtraCostFormErrors(),
    isEditMode: Boolean = false,
    isLoading: Boolean = false
) {
    if (isVisible) {
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
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight()
                ) {
                    // ═══════════════════════════════════════════════════════
                    // HEADER
                    // ═══════════════════════════════════════════════════════
                    Row(  // ✅ Cambiar de Box a Row
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFFF5F5F5),
                                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                            )
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isEditMode) "Editar Cost Extra" else "Agregar Costo Extra",//"✏️ Edit Extra Cost" else "➕ Add Extra Cost",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            onClick = onCancelClick,
                            enabled = !isLoading,
                            modifier = Modifier.padding(0.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Cerrar",
                                tint = Color(0xFF666666)
                            )
                        }
                    }

                    // ═══════════════════════════════════════════════════════
                    // CONTENT (SCROLLABLE)
                    // ═══════════════════════════════════════════════════════
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        ExtraCostFormFields(
                            formData = formData,
                            onQuantityChange = onQuantityChange,
                            onCategoryChange = onCategoryChange,
                            onDescriptionChange = onDescriptionChange,
                            onNotesChange = onNotesChange,
                            errors = mapOf(
                                "quantity" to errors.quantityError,
                                "category" to errors.categoryError,
                                "description" to errors.descriptionError
                            )
                        )
                    }

                    // ═══════════════════════════════════════════════════════
                    // FOOTER (ACTION BUTTONS)
                    // ═══════════════════════════════════════════════════════
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFFFAFAFA),
                                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Cancel Button
                            Button(
                                onClick = onCancelClick,
                                enabled = !isLoading,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFE0E0E0),
                                    disabledContainerColor = Color(0xFFEEEEEE)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Cancelar",
                                    color = Color(0xFF666666),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            // Save Button
                            Button(
                                onClick = onSaveClick,
                                enabled = !errors.hasErrors() && !isLoading,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF1976D2),
                                    disabledContainerColor = Color(0xFFBBBBBB)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                if (isLoading) {
                                    Text(
                                        text = if (isEditMode) "Actualizando..." else "Guardando...",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                } else {
                                    Text(
                                        text = if (isEditMode) "Actualizar" else "Guardar",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════
// PREVIEW - ADD MODE
// ═══════════════════════════════════════════════════════
@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun PreviewExtraCostModalAdd() {
    ExtraCostModal(
        isVisible = true,
        formData = ExtraCostUIModel(
            quantity = 0.0,
            category = ExtraCostCategory.SPARE_PARTS,
            description = "",
            notes = ""
        ),
        onQuantityChange = {},
        onCategoryChange = {},
        onDescriptionChange = {},
        onNotesChange = {},
        onSaveClick = {},
        onCancelClick = {},
        isEditMode = false
    )
}

// ═══════════════════════════════════════════════════════
// PREVIEW - EDIT MODE WITH DATA
// ═══════════════════════════════════════════════════════
@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun PreviewExtraCostModalEdit() {
    ExtraCostModal(
        isVisible = true,
        formData = ExtraCostUIModel(
            quantity = 45.50,
            category = ExtraCostCategory.SPARE_PARTS,
            description = "Synthetic oil 5W-30",
            notes = "Recommended by manufacturer"
        ),
        onQuantityChange = {},
        onCategoryChange = {},
        onDescriptionChange = {},
        onNotesChange = {},
        onSaveClick = {},
        onCancelClick = {},
        isEditMode = true
    )
}

// ═══════════════════════════════════════════════════════
// PREVIEW - WITH ERRORS
// ═══════════════════════════════════════════════════════
@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun PreviewExtraCostModalWithErrors() {
    ExtraCostModal(
        isVisible = true,
        formData = ExtraCostUIModel(
            quantity = 0.0,
            category = ExtraCostCategory.SPARE_PARTS,
            description = "",
            notes = ""
        ),
        onQuantityChange = {},
        onCategoryChange = {},
        onDescriptionChange = {},
        onNotesChange = {},
        onSaveClick = {},
        onCancelClick = {},
        errors = ExtraCostFormErrors(
            quantityError = "Amount is required",
            descriptionError = "Description cannot be empty"
        ),
        isEditMode = false
    )
}

// ═══════════════════════════════════════════════════════
// PREVIEW - LOADING STATE
// ═══════════════════════════════════════════════════════
@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun PreviewExtraCostModalLoading() {
    ExtraCostModal(
        isVisible = true,
        formData = ExtraCostUIModel(
            quantity = 45.50,
            category = ExtraCostCategory.SPARE_PARTS,
            description = "Synthetic oil 5W-30",
            notes = ""
        ),
        onQuantityChange = {},
        onCategoryChange = {},
        onDescriptionChange = {},
        onNotesChange = {},
        onSaveClick = {},
        onCancelClick = {},
        isLoading = true,
        isEditMode = false
    )
}
