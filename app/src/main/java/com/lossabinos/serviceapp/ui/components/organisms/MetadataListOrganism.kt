package com.lossabinos.serviceapp.ui.components.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lossabinos.domain.entities.Metadata
import com.lossabinos.serviceapp.models.MetadataModel
import com.lossabinos.serviceapp.ui.components.molecules.MetadataCardMolecule
import com.lossabinos.serviceapp.ui.components.molecules.MetadataItemCardMolecule

@Composable
fun MetadataListOrganism(
    metadata: List<MetadataModel>,
    modifier: Modifier = Modifier,
    title: String = "NOTAS Y REQUISITOS",  // ← IGUAL QUE "ACTIVIDADES"
    icon: ImageVector = Icons.AutoMirrored.Filled.Notes
) {
    if (metadata.isEmpty()) {
        return
    }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)  // ← IGUAL
    ) {
        // ═════════════════════════════════════════════════════
        // TÍTULO (IGUAL QUE "ACTIVIDADES")
        // ═════════════════════════════════════════════════════
        Text(
            text = title,
            fontSize = 16.sp,  // ← IGUAL
            fontWeight = FontWeight.Bold,  // ← IGUAL
            color = Color.Black,
            modifier = Modifier.padding(start = 4.dp)  // ← IGUAL
        )

        // ═════════════════════════════════════════════════════
        // LISTA DE METADATA (CARDS IGUALES A ACTIVIDADES)
        // ═════════════════════════════════════════════════════
        metadata.forEach { item ->
            MetadataItemCardMolecule(
                name = item.name,
                value = item.value,
                icon = icon,  // ← OPCIONAL: cambiar icono por item si quieres
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
/*
@Composable
fun MetadataListOrganism(
    metadata: List<MetadataModel>,
    modifier: Modifier = Modifier,
    title: String = "Notas y Requisitos",
    icon: ImageVector = Icons.AutoMirrored.Filled.Notes
) {
    if (metadata.isEmpty()) {
        return
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ═════════════════════════════════════════════════════
        // ENCABEZADO
        // ═════════════════════════════════════════════════════
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color(0xFFFFC107)
            )

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        // ═════════════════════════════════════════════════════
        // LISTA DE METADATA
        // ═════════════════════════════════════════════════════
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            metadata.forEach { item ->
                MetadataCardMolecule(
                    name = item.name,
                    value = item.value,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
*/