package com.lossabinos.serviceapp.ui.components.molecules

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import java.io.File

@Composable
fun PhotoImageWithZoom(
    imagePath: String,
    zoomState: Float,
    onZoomChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {

    val painter = rememberAsyncImagePainter(
        model = File(imagePath),
        contentScale = ContentScale.Fit
    )

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    // 🔧 RESETEAR offsets cuando zoom vuelve a 1f
    LaunchedEffect(zoomState) {
        if (zoomState == 1f) {
            offsetX = 0f
            offsetY = 0f
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures(
                    panZoomLock = false,  // ← Permite zoom y pan independientes
                    onGesture = { _, pan, zoom, _ ->
                        if (zoom != 1f) {  // Solo si hay zoom real
                            val newZoom = (zoomState * zoom).coerceIn(1f, 10f)
                            onZoomChange(newZoom)

                            // Pan solo si hay zoom
                            if (newZoom > 1f) {
                                offsetX += pan.x
                                offsetY += pan.y
                            }
                        }
                    }
                )
                /*
                detectTransformGestures { _, pan, zoom, _ ->
                    //val newZoom = (zoomState * zoom).coerceIn(1f, 10f)
                    val newZoom = (zoomState * zoom * 1.5f).coerceIn(1f, 10f)
                    onZoomChange(newZoom)

                    // ✅ SOLO permitir movimiento si hay zoom
                    if (newZoom > 1f) {
                        offsetX += pan.x
                        offsetY += pan.y
                    }
                }*/
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painter,
            contentDescription = "Foto evidencia",
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = zoomState,
                    scaleY = zoomState,
                    translationX = offsetX,
                    translationY = offsetY
                ),
            contentScale = ContentScale.Fit
        )

        // Loading
        if (painter.state is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator(color = Color.White)
        }

        // Error
        if (painter.state is AsyncImagePainter.State.Error) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    Icons.Default.BrokenImage,
                    "Error",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
                Text("Error cargando imagen", color = Color.White, fontSize = 12.sp)
            }
        }
    }


    /*
        val painter = rememberAsyncImagePainter(
            model = File(imagePath),
            contentScale = ContentScale.Fit
        )

        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }

        Box(
            modifier = modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        onZoomChange((zoomState * zoom).coerceIn(1f, 5f))
                        offsetX += pan.x
                        offsetY += pan.y
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painter,
                contentDescription = "Foto evidencia",
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = zoomState,
                        scaleY = zoomState,
                        translationX = offsetX,
                        translationY = offsetY
                    ),
                contentScale = ContentScale.Fit
            )

            // Loading
            if (painter.state is AsyncImagePainter.State.Loading) {
                CircularProgressIndicator(color = Color.White)
            }

            // Error
            if (painter.state is AsyncImagePainter.State.Error) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        Icons.Default.BrokenImage,
                        "Error",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                    Text("Error cargando imagen", color = Color.White, fontSize = 12.sp)
                }
            }
        }
     */
}
