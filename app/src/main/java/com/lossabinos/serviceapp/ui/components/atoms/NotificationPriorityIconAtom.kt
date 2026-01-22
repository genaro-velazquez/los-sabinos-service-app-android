package com.lossabinos.serviceapp.ui.components.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lossabinos.domain.enums.NotificationPriority


@Composable
fun NotificationPriorityIconAtom(
    priority: NotificationPriority,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, iconColor, icon) = when (priority) {
        NotificationPriority.LOW -> Triple(
            Color(0xFFE8F5E9),
            Color(0xFF2E7D32),
            Icons.Filled.Info
        )
        NotificationPriority.NORMAL -> Triple(
            Color(0xFFFFF3E0),
            Color(0xFFE65100),
            Icons.Filled.Warning
        )
        NotificationPriority.HIGH -> Triple(
            Color(0xFFFFEBEE),
            Color(0xFFC62828),
            Icons.Filled.Error
        )
        NotificationPriority.UNKNOWN -> Triple(
            Color(0xFF8B0000),
            Color(0xFFFFFFFF),
            Icons.Filled.Error
        )
    }

    Box(
        modifier = modifier
            .size(32.dp)
            .background(backgroundColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Priority: $priority",
            tint = iconColor,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationPriorityIconAtomPreview(){
    MaterialTheme{
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            NotificationPriorityIconAtom(priority = NotificationPriority.LOW)
            NotificationPriorityIconAtom(priority = NotificationPriority.NORMAL)
            NotificationPriorityIconAtom(priority = NotificationPriority.HIGH)
            NotificationPriorityIconAtom(priority = NotificationPriority.UNKNOWN)
        }
    }
}