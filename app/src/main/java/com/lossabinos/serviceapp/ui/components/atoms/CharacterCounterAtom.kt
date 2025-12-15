package com.lossabinos.serviceapp.ui.components.atoms

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun CharacterCounterAtom(
    currentCount: Int,
    maxCount: Int = 300,
    modifier: Modifier = Modifier
) {
    val isNearLimit = currentCount > maxCount * 0.8  // Más de 80%
    val counterColor = if (isNearLimit && currentCount >= maxCount) {
        Color(0xFFE53935)  // Rojo si llegó al límite
    } else if (isNearLimit) {
        Color(0xFFFFA500)  // Naranja si está cerca
    } else {
        Color(0xFF9E9E9E)  // Gris normal
    }

    Text(
        text = "$currentCount/$maxCount",
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = counterColor,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun CharacterCounterAtomPreview(){
    MaterialTheme{
        CharacterCounterAtom(
            currentCount = 12
        )
    }
}