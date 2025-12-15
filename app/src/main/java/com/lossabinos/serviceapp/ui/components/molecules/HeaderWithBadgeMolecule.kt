package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lossabinos.serviceapp.ui.components.atoms.BadgeProgress
import com.lossabinos.serviceapp.ui.components.atoms.ServiceTitle

@Composable
fun HeaderWithBadgeMolecule(
    title: String,
    subtitle: String,
    badge: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        // 1️⃣ Title + Subtitle
        ServiceTitle(
            title = title,
            subtitle = subtitle,
            modifier = Modifier.weight(1f)
        )

        // 2️⃣ Badge
        BadgeProgress(text = badge)
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderWithBadgeMoleculePreview(){
    MaterialTheme{
        HeaderWithBadgeMolecule(
            title = "titulo",
            subtitle = "sub titulo",
            badge = "en progreso"
        )
    }
}