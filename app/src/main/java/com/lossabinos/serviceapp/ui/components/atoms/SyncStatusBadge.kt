package com.lossabinos.serviceapp.ui.components.atoms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class SyncStatusConfig(
    val backgroundColor: Color,
    val textColor: Color,
    val icon: ImageVector,
    val label: String
)

@Composable
fun SyncStatusBadge(
    syncStatus: String,  // "SYNCED", "PENDING", "ERROR"
    modifier: Modifier = Modifier
) {
    val config = when (syncStatus) {
        "SYNCED" -> SyncStatusConfig(
            backgroundColor = Color(0xFFE8F5E9),
            textColor = Color(0xFF2E7D32),
            icon = Icons.Default.Check,
            label = "Sincronizado"
        )
        "PENDING" -> SyncStatusConfig(
            backgroundColor = Color(0xFFFFF3E0),
            textColor = Color(0xFFE65100),
            icon = Icons.Default.Sync,
            label = "Pendiente"
        )
        "ERROR" -> SyncStatusConfig(
            backgroundColor = Color(0xFFFFEBEE),
            textColor = Color(0xFFC62828),
            icon = Icons.Default.Close,
            label = "Error"
        )
        else -> SyncStatusConfig(
            backgroundColor = Color(0xFFE0E0E0),
            textColor = Color(0xFF424242),
            icon = Icons.Default.Sync,
            label = "Desconocido"
        )
    }

    Surface(
        modifier = modifier
            .wrapContentWidth()
            .height(28.dp),
        shape = RoundedCornerShape(12.dp),
        color = config.backgroundColor
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = config.icon,  // ✅ Sin cast
                contentDescription = config.label,
                tint = config.textColor,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = config.label,
                style = MaterialTheme.typography.labelSmall,
                color = config.textColor,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
