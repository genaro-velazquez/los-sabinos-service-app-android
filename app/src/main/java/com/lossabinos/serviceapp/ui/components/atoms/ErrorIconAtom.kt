package com.lossabinos.serviceapp.ui.components.atoms

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ErrorIconAtom(
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = Icons.Filled.ErrorOutline,
        contentDescription = "Error",
        tint = Color(0xFFD32F2F),  // Rojo
        modifier = modifier
            .size(64.dp)
    )
}
