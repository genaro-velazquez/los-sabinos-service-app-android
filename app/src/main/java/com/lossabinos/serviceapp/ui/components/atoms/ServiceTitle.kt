package com.lossabinos.serviceapp.ui.components.atoms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ServiceTitle(
    title: String,
    subtitle: String,
    titleSize: TextUnit = 22.sp,
    subtitleSize: TextUnit = 14.sp,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            fontSize = titleSize,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = subtitle,
            fontSize = subtitleSize,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF9E9E9E),  // Gris
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun serviceTitlePreview(){
    MaterialTheme{
        ServiceTitle(
            title = "Esto es el titulo",
            subtitle = "Esto es el sub titulo"
        )
    }
}