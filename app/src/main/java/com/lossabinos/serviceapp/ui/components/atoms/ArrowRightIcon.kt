package com.lossabinos.serviceapp.ui.components.atoms

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ArrowRightIcon(
    size: Dp = 24.dp,
    color: Color = Color.Black,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
        contentDescription = "Continuar",
        modifier = modifier.size(size),
        tint = color
    )
}