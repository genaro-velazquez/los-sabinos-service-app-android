package com.lossabinos.serviceapp.ui.components.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ═══════════════════════════════════════════════════════
// TEXT INPUT ATOM (Reutilizable para descripción y notas)
// ═══════════════════════════════════════════════════════
@Composable
fun TextInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isError: Boolean = false,
    maxLines: Int = 1,
    maxLength: Int = Int.MAX_VALUE
) {
    BasicTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.length <= maxLength) {
                onValueChange(newValue)
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = if (isError) Color(0xFFD32F2F) else Color(0xFFBBBBBB),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            )
            .background(Color.White)
            .padding(12.dp),
        textStyle = TextStyle(
            fontSize = 16.sp,
            color = Color.Black
        ),
        decorationBox = { innerTextField ->
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    fontSize = 16.sp,
                    color = Color(0xFFAAAAAA)
                )
            }
            innerTextField()
        },
        maxLines = maxLines,
        singleLine = maxLines == 1
    )
}
