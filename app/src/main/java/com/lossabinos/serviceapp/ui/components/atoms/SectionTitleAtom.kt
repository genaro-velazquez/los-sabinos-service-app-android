package com.lossabinos.serviceapp.ui.components.atoms

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SectionTitleAtom(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = modifier.padding(bottom = 12.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun SectionTitleAtomPreview(){
    MaterialTheme {
        SectionTitleAtom(
            title = "Titulo de la sección"
        )
    }
}