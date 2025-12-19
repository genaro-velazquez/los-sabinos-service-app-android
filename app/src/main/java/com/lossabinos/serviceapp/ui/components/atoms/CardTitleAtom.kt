package com.lossabinos.serviceapp.ui.components.atoms

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp


@Composable
fun CardTitleAtom(
    title: String,
    fontSize: TextUnit = 18.sp,
    fontWeight: FontWeight = FontWeight.Bold
) {
    Text(
        text = title,
        fontSize = fontSize,
        fontWeight = fontWeight,
        color = Color.Black
    )
}