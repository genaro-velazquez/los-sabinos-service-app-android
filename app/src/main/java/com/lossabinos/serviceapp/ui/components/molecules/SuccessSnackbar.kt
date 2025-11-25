package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.theme.SuccessGreen

@Composable
fun SuccessSnackbar(snackbarData: SnackbarData) {
    Snackbar(
        containerColor = SuccessGreen,  // Verde
        content = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.CheckCircle, contentDescription = "Éxito", tint = Color.White)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = snackbarData.visuals.message, color = Color.White)
            }
        }
    )
}
