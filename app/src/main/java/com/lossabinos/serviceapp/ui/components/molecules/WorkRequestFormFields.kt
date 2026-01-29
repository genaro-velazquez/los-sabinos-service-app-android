package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lossabinos.serviceapp.models.ui.UrgencyUI
import com.lossabinos.serviceapp.models.ui.WorkRequestUIModel
import com.lossabinos.serviceapp.ui.components.atoms.CheckboxRow
import com.lossabinos.serviceapp.ui.components.atoms.ErrorText
import com.lossabinos.serviceapp.ui.components.atoms.TextInput

@Composable
fun WorkRequestFormFields(
    formData: WorkRequestUIModel,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onFindingsChange: (String) -> Unit,
    onJustificationChange: (String) -> Unit,
    onUrgencyChange: (UrgencyUI) -> Unit,
    onRequiresApprovalChange: (Boolean) -> Unit,
    errors: Map<String, String?> = emptyMap(),
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        // ═══════════════════════════════════════════════════════
        // FIELD 1: TITLE
        // ═══════════════════════════════════════════════════════
        Text(
            text = "Título * (Requerido)",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        TextInput(
            value = formData.title,
            onValueChange = onTitleChange,
            placeholder = "Ej. Cambio de balatas",
            isError = errors["title"] != null,
            maxLength = 100,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        ErrorText(
            error = errors["title"],
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // ═══════════════════════════════════════════════════════
        // FIELD 2: DESCRIPTION
        // ═══════════════════════════════════════════════════════
        Text(
            text = "Descripción * (Requerido)",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        TextInput(
            value = formData.description,
            onValueChange = onDescriptionChange,
            placeholder = "Describe el problema detectado",
            isError = errors["description"] != null,
            maxLines = 3,
            maxLength = 300,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        ErrorText(
            error = errors["description"],
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // ═══════════════════════════════════════════════════════
        // FIELD 3: FINDINGS
        // ═══════════════════════════════════════════════════════
        Text(
            text = "Hallazgos * (Requerido)",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        TextInput(
            value = formData.findings,
            onValueChange = onFindingsChange,
            placeholder = "¿Qué encontraste?",
            isError = errors["findings"] != null,
            maxLines = 3,
            maxLength = 300,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        ErrorText(
            error = errors["findings"],
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // ═══════════════════════════════════════════════════════
        // FIELD 4: JUSTIFICATION
        // ═══════════════════════════════════════════════════════
        Text(
            text = "Justificación * (Requerido)",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        TextInput(
            value = formData.justification,
            onValueChange = onJustificationChange,
            placeholder = "¿Por qué es necesario?",
            isError = errors["justification"] != null,
            maxLines = 3,
            maxLength = 300,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        ErrorText(
            error = errors["justification"],
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // ═══════════════════════════════════════════════════════
        // FIELD 5: URGENCY
        // ═══════════════════════════════════════════════════════
        Text(
            text = "Urgencia",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        UrgencySelector(
            selected = formData.urgency,
            onChange = onUrgencyChange,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // ═══════════════════════════════════════════════════════
        // FIELD 6: CUSTOMER APPROVAL
        // ═══════════════════════════════════════════════════════
        CheckboxRow(
            checked = formData.requiresCustomerApproval,
            label = "Requiere aprobación del cliente",
            onCheckedChange = onRequiresApprovalChange
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    widthDp = 360,
    heightDp = 700
)
@Composable
fun PreviewWorkRequestFormFields() {
    val mockForm = WorkRequestUIModel(
        title = "Cambio de balatas",
        description = "Se detecta desgaste excesivo",
        findings = "Balatas al 10%",
        justification = "Riesgo de frenado",
        urgency = UrgencyUI.HIGH,
        requiresCustomerApproval = true
    )

    WorkRequestFormFields(
        formData = mockForm,
        onTitleChange = {},
        onDescriptionChange = {},
        onFindingsChange = {},
        onJustificationChange = {},
        onUrgencyChange = {},
        onRequiresApprovalChange = {}
    )
}

