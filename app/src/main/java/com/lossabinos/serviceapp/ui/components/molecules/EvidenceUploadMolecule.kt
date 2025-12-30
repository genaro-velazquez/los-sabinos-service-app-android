package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lossabinos.serviceapp.ui.components.atoms.EvidenceBadge
import com.lossabinos.serviceapp.ui.components.atoms.PhotoPlaceholder
import com.lossabinos.serviceapp.ui.components.atoms.TaskPhotoItem

@Composable
fun EvidenceUploadMolecule(
    hasPhoto: Boolean,
    photoUri: String? = null,
    onAddPhoto: () -> Unit,
    onRemovePhoto: (() -> Unit)? = null,
    onPhotoClick: (String) -> Unit = {},
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
            //TaskPhotoItem(imageUri = photoUri)
            Box(modifier = Modifier.fillMaxWidth()) {
                TaskPhotoItem(
                    imageUri = photoUri,
                    onPhotoClick = onPhotoClick
                )

                // Botón eliminar (esquina superior derecha)
                if (onRemovePhoto != null) {
                    IconButton(
                        onClick = onRemovePhoto,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .background(
                                color = Color.White.copy(alpha = 0.9f),
                                shape = RoundedCornerShape(20.dp)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Eliminar foto",
                            tint = Color.Red
                        )
                    }
                }
            }
        } else {
            // Mostrar placeholder
            PhotoPlaceholder(onAddPhoto = onAddPhoto)
        }
    }
}