package com.lossabinos.serviceapp.ui.components.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ═══════════════════════════════════════════════════════
// CURRENCY INPUT ATOM
// ═══════════════════════════════════════════════════════
@Composable
fun CurrencyInput(
    value: Double,
    onValueChange: (Double) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    placeholder: String = "0.00"
) {
    val textValue = if (value == 0.0) "" else value.toString()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = if (isError) Color(0xFFD32F2F) else Color(0xFFBBBBBB),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            )
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Símbolo $
        Text(
            text = "$",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(end = 8.dp)
        )

        // Input field
        BasicTextField(
            value = textValue,
            onValueChange = { newValue ->
                // Solo permitir números y punto decimal
                if (newValue.isEmpty()) {
                    onValueChange(0.0)
                } else {
                    val filtered = newValue.filter { it.isDigit() || it == '.' }
                    val doubleValue = filtered.toDoubleOrNull() ?: 0.0
                    onValueChange(doubleValue)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            textStyle = androidx.compose.material3.LocalTextStyle.current.copy(
                fontSize = 16.sp,
                color = Color.Black
            ),
            decorationBox = { innerTextField ->
                if (textValue.isEmpty()) {
                    Text(
                        text = placeholder,
                        fontSize = 16.sp,
                        color = Color(0xFFAAAAAA)
                    )
                }
                innerTextField()
            },
            singleLine = true
        )
    }
}