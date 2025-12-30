package com.lossabinos.serviceapp.screens.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lossabinos.serviceapp.ui.components.molecules.PhotoViewerBottomBar
import com.lossabinos.serviceapp.ui.components.molecules.PhotoViewerTopBar
import com.lossabinos.serviceapp.ui.components.organisms.PhotoViewerContent

@Composable
fun PhotoViewerDialog(
    photoPaths: List<String>,
    initialIndex: Int = 0,
    onDismiss: () -> Unit,
    onDelete: (String) -> Unit = {},
    showDeleteButton: Boolean = false
) {
    var currentIndex by remember { mutableStateOf(initialIndex) }
    var zoomState by remember { mutableStateOf(1f) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
            PhotoViewerContent(
                photoPaths = photoPaths,
                initialIndex = initialIndex,
                zoomState = zoomState,
                onZoomChange = { zoomState = it },
                onIndexChange = { currentIndex = it }
            )

            PhotoViewerTopBar(
                currentIndex = currentIndex,
                totalPhotos = photoPaths.size,
                onDismiss = onDismiss,
                modifier = Modifier.align(Alignment.TopCenter)
            )

            if (photoPaths.isNotEmpty()) {
                PhotoViewerBottomBar(
                    currentIndex = currentIndex,
                    currentPhotoPath = photoPaths[currentIndex],
                    showDeleteButton = showDeleteButton,
                    onDelete = {
                        onDelete(photoPaths[currentIndex])
                        onDismiss()
                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}
