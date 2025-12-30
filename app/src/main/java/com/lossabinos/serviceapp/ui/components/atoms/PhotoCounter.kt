package com.lossabinos.serviceapp.ui.components.atoms

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun PhotoCounter(
    current: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = "${current + 1} / $total",
        color = Color.White,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}
