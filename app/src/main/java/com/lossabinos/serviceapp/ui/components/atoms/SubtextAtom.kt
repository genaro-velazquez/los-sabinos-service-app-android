package com.lossabinos.serviceapp.ui.components.atoms

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun SubtextAtom(
    text: String,
    color: Color = Color(0xFF9E9E9E),
    fontSize: TextUnit = 12.sp
) {
    Text(
        text = text,
        fontSize = fontSize,
        color = color
    )
}