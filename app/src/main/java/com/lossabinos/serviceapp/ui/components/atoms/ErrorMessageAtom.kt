package com.lossabinos.serviceapp.ui.components.atoms

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun ErrorMessageAtom(
    message: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = message,
        style = MaterialTheme.typography.bodyMedium,
        color = Color(0xFF757575),  // Gris oscuro
        fontSize = 14.sp,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}
