package com.lossabinos.serviceapp.ui.components.atoms

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import java.io.File


@Composable
fun TaskPhotoItem(
    imageUri: String,
    modifier: Modifier = Modifier,
    onPhotoClick: (String) -> Unit = {}
) {
    println("📸 TaskPhotoItem cargando: $imageUri")

    // ✅ Limpiar URI si viene con file://
    val cleanPath = if (imageUri.startsWith("file://")) {
        imageUri.removePrefix("file://")
    } else {
        imageUri
    }

    val imageFile = File(cleanPath)
    println("   Ruta limpia: ${imageFile.absolutePath}")
    println("   ¿Existe archivo? ${imageFile.exists()}")
    println("   Tamaño: ${imageFile.length()} bytes")

    // ✅ Usar rememberAsyncImagePainter para acceder al estado
    val painter = rememberAsyncImagePainter(
        model = imageFile,
        contentScale = ContentScale.Crop,
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable{
                onPhotoClick(imageUri)
            },
        contentAlignment = Alignment.BottomEnd
    ) {
        // Mostrar imagen
        Image(
            painter = painter,
            contentDescription = "Foto capturada",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Fit
        )

        // ✅ Mostrar LOADING
        if (painter.state is AsyncImagePainter.State.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Blue)
            }
        }

        // ✅ Mostrar ERROR
        if (painter.state is AsyncImagePainter.State.Error) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.BrokenImage,
                        contentDescription = "Error",
                        tint = Color.Red,
                        modifier = Modifier.size(40.dp)
                    )
                    Text(
                        "Error: ${(painter.state as AsyncImagePainter.State.Error).result.throwable.message}",
                        color = Color.Red,
                        fontSize = 10.sp
                    )
                    Text(
                        "Ruta: $imageUri",
                        fontSize = 9.sp,
                        color = Color.Red,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        "Existe: ${imageFile.exists()}",
                        fontSize = 9.sp,
                        color = Color.Red
                    )
                }
            }
        }

        // Badge verde
        Box(
            modifier = Modifier
                .padding(8.dp)
                .size(36.dp)
                .background(
                    color = Color(0xFF4CAF50),
                    shape = CircleShape
                )
                .shadow(elevation = 4.dp, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Foto capturada",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}



/*
@Composable
fun TaskPhotoItem(
    imageUri: String,  // URI de la foto capturada
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(
                color = Color.Gray,
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.BottomEnd
    ) {
        // Mostrar imagen (coil o similar)
         AsyncImage(
             model = imageUri,
             contentDescription = "Foto capturada",
             contentScale = ContentScale.Fit,
             modifier = Modifier.fillMaxSize()
        )

        // Badge verde con checkmark
        Box(
            modifier = Modifier
                .padding(8.dp)
                .size(36.dp)
                .background(
                    color = Color(0xFF4CAF50),  // Verde
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Foto capturada",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
*/
@Preview(showBackground = true)
@Composable
fun TaskPhotoItemPreview(){
    MaterialTheme{
        TaskPhotoItem(imageUri = "")
    }
}