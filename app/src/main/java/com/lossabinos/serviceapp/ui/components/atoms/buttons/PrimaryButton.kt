package com.lossabinos.serviceapp.ui.components.atoms.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme  // ✅ AGREGAR
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lossabinos.serviceapp.ui.theme.PrimaryYellow
import com.lossabinos.serviceapp.ui.theme.TextBlack

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    val buttonColor = PrimaryYellow
    val textColor = TextBlack

    Box(
        modifier = modifier
            .height(56.dp)
            .background(
                color = buttonColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(enabled = enabled, onClick = onClick),  // ✅ AGREGAR enabled
        contentAlignment = Alignment.Center,
        content = {
            if (isLoading) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = textColor,
                        strokeWidth = 2.dp
                    )
                    Text(
                        "Cargando...",
                        color = textColor,
                        fontSize = 14.sp
                    )
                }
            } else {
                Text(
                    text,
                    color = textColor,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    )
}