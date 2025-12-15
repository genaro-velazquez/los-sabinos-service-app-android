package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.components.atoms.ProgressBar
import com.lossabinos.serviceapp.ui.components.atoms.ProgressLabel

@Composable
fun ProgressIndicatorMolecule(
    label: String,
    percentage: Int,  // 0-100 (ej: 35)
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // 1️⃣ Label + Percentage
        ProgressLabel(
            label = label,
            percentage = "$percentage%"
        )

        // 2️⃣ Progress Bar
        Spacer(modifier = Modifier.height(8.dp))

        ProgressBar(
            progress = percentage / 100f
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProgressIndicatorPreview(){
    MaterialTheme{
        ProgressIndicatorMolecule(
            label = "cumplimiento",
            percentage = 60
        )
    }
}