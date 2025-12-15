package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.components.atoms.EvidenceBadge
import com.lossabinos.serviceapp.ui.components.atoms.PhotoPlaceholder
import com.lossabinos.serviceapp.ui.components.atoms.TaskPhotoItem

@Composable
fun EvidenceUploadMolecule(
    hasPhoto: Boolean,
    photoUri: String? = null,
    onAddPhoto: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        EvidenceBadge()

        if (hasPhoto && photoUri != null) {
            // Mostrar foto capturada
            TaskPhotoItem(imageUri = photoUri)
        } else {
            // Mostrar placeholder
            PhotoPlaceholder(onAddPhoto = onAddPhoto)
        }
    }
}