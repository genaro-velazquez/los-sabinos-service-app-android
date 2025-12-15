package com.lossabinos.serviceapp.ui.components.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ObservationTextInputAtom(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Escribe tus observaciones aquí...",
    maxLines: Int = 6,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = Color(0xFFBDBDBD),  // Gris claro
                fontSize = 14.sp
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color(0xFFFFC107),  // Amarillo
            unfocusedIndicatorColor = Color(0xFFE0E0E0),  // Gris claro
            cursorColor = Color.Black
        ),
        shape = RoundedCornerShape(12.dp),
        maxLines = maxLines,
        textStyle = TextStyle(
            fontSize = 14.sp,
            color = Color.Black
        )
    )
}

@Preview(showBackground = true)
@Composable
fun ObservationTextInputAtomReview(){
    MaterialTheme{
        ObservationTextInputAtom(
            value = "",
            onValueChange = { _ -> }
        )
    }
}