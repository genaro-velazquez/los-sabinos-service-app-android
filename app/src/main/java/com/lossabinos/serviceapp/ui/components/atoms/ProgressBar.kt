package com.lossabinos.serviceapp.ui.components.atoms

import android.widget.ProgressBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ProgressBar(
    progress: Float,  // 0f - 1f (ej: 0.35f)
    progressColor: Color = MaterialTheme.colorScheme.primary,  // Amarillo
    backgroundColor: Color = Color(0xFFE0E0E0),  // Gris claro
    height: Dp = 8.dp,
    cornerRadius: Dp = 4.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(cornerRadius)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = progress.coerceIn(0f, 1f))
                .background(
                    color = progressColor,
                    shape = RoundedCornerShape(cornerRadius)
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProgressBarPreview(){
    MaterialTheme{
        ProgressBar(progress = 0.3f)
    }
}