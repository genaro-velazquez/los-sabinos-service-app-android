package com.lossabinos.serviceapp.ui.components.molecules

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
import com.lossabinos.serviceapp.models.ui.ExtraCostCategory
import com.lossabinos.serviceapp.models.ui.ExtraCostUIModel
import com.lossabinos.serviceapp.ui.components.atoms.CategoryDropdown
import com.lossabinos.serviceapp.ui.components.atoms.CurrencyInput
import com.lossabinos.serviceapp.ui.components.atoms.ErrorText
import com.lossabinos.serviceapp.ui.components.atoms.TextInput

// ═══════════════════════════════════════════════════════
// EXTRA COST FORM FIELDS MOLECULE
// ═══════════════════════════════════════════════════════
@Composable
fun ExtraCostFormFields(
    formData: ExtraCostUIModel,
    onQuantityChange: (Double) -> Unit,
    onCategoryChange: (ExtraCostCategory) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    errors: Map<String, String?> = emptyMap(),
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // ═══════════════════════════════════════════════════════
        // FIELD 1: QUANTITY
        // ═══════════════════════════════════════════════════════
        /*
        Text(
            text = "Amount * (Required)",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        CurrencyInput(
            value = formData.quantity,
            onValueChange = onQuantityChange,
            placeholder = "0.00",
            isError = errors["quantity"] != null,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        ErrorText(
            error = errors["quantity"],
            modifier = Modifier.padding(bottom = 16.dp)
        )
        */

        // ═══════════════════════════════════════════════════════
        // FIELD 2: CATEGORY
        // ═══════════════════════════════════════════════════════
        Text(
            text = "Category * (Required)",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        CategoryDropdown(
            selectedCategory = formData.category,
            onCategorySelected = onCategoryChange,
            isError = errors["category"] != null,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        ErrorText(
            error = errors["category"],
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // ═══════════════════════════════════════════════════════
        // FIELD 3: DESCRIPTION
        // ═══════════════════════════════════════════════════════
        Text(
            text = "Description * (Required)",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        TextInput(
            value = formData.description,
            onValueChange = onDescriptionChange,
            placeholder = "Enter description...",
            isError = errors["description"] != null,
            maxLength = 200,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        ErrorText(
            error = errors["description"],
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // ═══════════════════════════════════════════════════════
        // FIELD 4: NOTES (Optional)
        // ═══════════════════════════════════════════════════════
        Text(
            text = "Notes (Optional)",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        TextInput(
            value = formData.notes,
            onValueChange = onNotesChange,
            placeholder = "Add any notes...",
            maxLines = 3,
            maxLength = 500,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Character counter for notes
        Text(
            text = "${formData.notes.length}/500",
            fontSize = 12.sp,
            color = Color(0xFFAAAAAA),
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

// ═══════════════════════════════════════════════════════
// PREVIEW
// ═══════════════════════════════════════════════════════
@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    widthDp = 360,
    heightDp = 600
)
@Composable
fun PreviewExtraCostFormFields() {
    val mockFormData = ExtraCostUIModel(
        quantity = 45.50,
        category = ExtraCostCategory.SPARE_PARTS,
        description = "Synthetic oil 5W-30",
        notes = "Recommended by manufacturer"
    )

    ExtraCostFormFields(
        formData = mockFormData,
        onQuantityChange = {},
        onCategoryChange = {},
        onDescriptionChange = {},
        onNotesChange = {}
    )
}

// ═══════════════════════════════════════════════════════
// PREVIEW WITH ERRORS
// ═══════════════════════════════════════════════════════
@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    widthDp = 360,
    heightDp = 600
)
@Composable
fun PreviewExtraCostFormFieldsWithErrors() {
    val mockFormData = ExtraCostUIModel(
        quantity = 0.0,
        category = ExtraCostCategory.SPARE_PARTS,
        description = "",
        notes = ""
    )

    val errors = mapOf(
        "quantity" to "Amount is required",
        "description" to "Description cannot be empty"
    )

    ExtraCostFormFields(
        formData = mockFormData,
        onQuantityChange = {},
        onCategoryChange = {},
        onDescriptionChange = {},
        onNotesChange = {},
        errors = errors
    )
}
