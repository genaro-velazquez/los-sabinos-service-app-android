package com.lossabinos.serviceapp.ui.components.organisms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HomeHeader(
    isOnline: Boolean,
    lastSyncLabel: String?
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Circle,
                tint = if (isOnline) Color.Green else Color.Red,
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
            Text(if (isOnline) "En línea" else "Sin conexión")
        }

        Text(
            text = "Última actualización: ${lastSyncLabel ?: "Nunca actualizado"}",
            style = MaterialTheme.typography.bodySmall
        )

        /*
                lastSyncLabel?.let {
                    Text(
                        text = "Última actualización: ${lastSyncLabel ?: "Nunca actualizado"}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
         */
    }
}
