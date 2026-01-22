package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lossabinos.domain.enums.NotificationCategory
import com.lossabinos.domain.enums.NotificationPriority
import com.lossabinos.domain.enums.NotificationType
import com.lossabinos.serviceapp.ui.components.atoms.NotificationDateAtom
import com.lossabinos.serviceapp.ui.components.atoms.NotificationPriorityIconAtom

data class NotificationItemUIModel(
    val id: String,
    val title: String,
    val message: String,
    val priority: NotificationPriority,
    val timestamp: Long,
    val isRead: Boolean = false,
    val type: NotificationType,           // ← NUEVO
    val category: NotificationCategory,   // ← NUEVO
    val actionUrl: String? = null         // ← NUEVO
)

@Composable
fun NotificationItemMolecule(
    notification: NotificationItemUIModel,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = if (notification.isRead) Color(0xFFFAFAFA) else Color(0xFFF5F5F5),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Icono de prioridad
        NotificationPriorityIconAtom(
            priority = notification.priority,
            modifier = Modifier.align(androidx.compose.ui.Alignment.Top)
        )

        // Contenido
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Título
            Text(
                text = notification.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121),
                maxLines = 1
            )

            // Mensaje
            Text(
                text = notification.message,
                fontSize = 12.sp,
                color = Color(0xFF616161),
                maxLines = 2
            )

            // Fecha
            NotificationDateAtom(
                timestamp = notification.timestamp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationItemMoleculePreview() {
    MaterialTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            NotificationItemMolecule(
                notification = NotificationItemUIModel(
                    id = "1",
                    title = "Servicio Completado",
                    message = "El servicio de mantenimiento ha sido completado exitosamente",
                    priority = NotificationPriority.LOW,
                    timestamp = System.currentTimeMillis(),
                    isRead = true,
                    type = NotificationType.NEW_NOTIFICATION,
                    category = NotificationCategory.USER
                )
            )

            NotificationItemMolecule(
                notification = NotificationItemUIModel(
                    id = "2",
                    title = "Falta de Documentación",
                    message = "Falta subir evidencia de fotos para este servicio",
                    priority = NotificationPriority.HIGH,
                    timestamp = System.currentTimeMillis() - 3600000,
                    isRead = false,
                    type = NotificationType.NEW_NOTIFICATION,
                    category = NotificationCategory.USER
                )
            )

            NotificationItemMolecule(
                notification = NotificationItemUIModel(
                    id = "3",
                    title = "Nuevo Servicio Asignado",
                    message = "Se te ha asignado un nuevo servicio de mantenimiento",
                    priority = NotificationPriority.NORMAL,
                    timestamp = System.currentTimeMillis() - 7200000,
                    isRead = true,
                    type = NotificationType.NEW_NOTIFICATION,
                    category = NotificationCategory.USER
                )
            )
        }
    }
}
