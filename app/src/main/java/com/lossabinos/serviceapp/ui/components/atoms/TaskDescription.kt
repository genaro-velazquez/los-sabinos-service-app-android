package com.lossabinos.serviceapp.ui.components.atoms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TaskDescription(
    description: String,
    status: String? = null,  // null o "Completado"
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = description,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )

        if (status != null) {
            Text(
                text = status,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF9E9E9E),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskDescriptionPreview(){
    MaterialTheme{
        TaskDescription(description = "descripción", status = "completado")
    }
}