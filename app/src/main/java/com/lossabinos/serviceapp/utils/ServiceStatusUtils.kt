package com.lossabinos.serviceapp.utils

import androidx.compose.ui.graphics.Color

data class StatusColor(
    val backgroundColor: Color,
    val textColor: Color,
    val displayName: String
)

fun getStatusColor(status: String?): StatusColor {
    return when (status?.lowercase()?.trim()) {
        "pending" -> StatusColor(
            backgroundColor = Color(0xFFfcf3d6),  // Amarillo
            textColor = Color(0xFFf1c231),
            displayName = "Pending"
        )
        "in_progress", "in progress" -> StatusColor(
            backgroundColor = Color(0xFFd7e5fd),  // azul
            textColor = Color(0xFF3C82F6),
            displayName = "In progress"
        )
        "finished", "complete" -> StatusColor(
            backgroundColor = Color(0xFFE6F4EA),  // Verde
            textColor = Color(0xFF2E7D32),
            displayName = "Finished"
        )
        "error" -> StatusColor(
            backgroundColor = Color(0xFFFDEAEA),  // Rojo claro (pastel)
            textColor = Color(0xFFD32F2F),        // Rojo intenso
            displayName = "Error"
        )
        "paused" -> StatusColor(
            backgroundColor = Color(0xFFF1F3F4),  // Gris muy claro
            textColor = Color(0xFF5F6368),        // Gris oscuro
            displayName = "Paused"
        )
        "canceled", "cancelled" -> StatusColor(
            backgroundColor = Color(0xFFF5EAEA),  // Gris rosado claro
            textColor = Color(0xFF9E4B4B),        // Rojo apagado
            displayName = "Canceled"
        )
        else -> StatusColor(
            backgroundColor = Color(0xFFE0E0E0),  // Gris default
            textColor = Color(0xFF424242),
            displayName = status ?: "Unknown"
        )
    }
}
