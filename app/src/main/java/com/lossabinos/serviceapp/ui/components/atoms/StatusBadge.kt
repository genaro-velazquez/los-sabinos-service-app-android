// presentation/ui/components/atoms/StatusBadge.kt
package com.lossabinos.serviceapp.ui.components.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.theme.OnlineGreen
import com.lossabinos.serviceapp.ui.theme.CardBackground

/**
 * Badge circular de estado (online/offline)
 *
 * @param isOnline Si está online (true) u offline (false)
 * @param modifier Modifier para personalización
 */
@Composable
fun StatusBadge(
    isOnline: Boolean = true,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isOnline) OnlineGreen else Color(0xFFCCCCCC)
    
    Box(
        modifier = modifier
            .background(backgroundColor, CircleShape)
            .border(2.dp, CardBackground, CircleShape)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {}
}
