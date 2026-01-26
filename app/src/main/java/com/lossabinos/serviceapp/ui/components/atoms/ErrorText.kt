package com.lossabinos.serviceapp.ui.components.atoms

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

// ═══════════════════════════════════════════════════════
// ERROR TEXT ATOM
// ═══════════════════════════════════════════════════════
@Composable
fun ErrorText(
    error: String?,
    modifier: Modifier = Modifier
) {
    if (!error.isNullOrEmpty()) {
        Text(
            text = "🔴 ⚠️ $error",
            color = Color(0xFFD32F2F),
            fontSize = 14.sp,
            modifier = modifier
        )
    }
}
