package com.lossabinos.serviceapp.ui.components.molecules

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.lossabinos.domain.entities.WorkRequestPhoto
import com.lossabinos.serviceapp.events.WorkRequestUiEvent
import com.lossabinos.serviceapp.models.ui.CategoryUI
import com.lossabinos.serviceapp.models.ui.UrgencyUI
import com.lossabinos.serviceapp.models.ui.WorkRequestUIModel
import com.lossabinos.serviceapp.states.WorkRequestFormErrors
import com.lossabinos.serviceapp.ui.components.atoms.ActionButtonAtom
import com.lossabinos.serviceapp.ui.components.atoms.CheckboxRow
import com.lossabinos.serviceapp.ui.components.atoms.ErrorText
import com.lossabinos.serviceapp.ui.components.atoms.TextInput
import com.lossabinos.serviceapp.ui.components.organisms.CategoryDropdown
import com.lossabinos.serviceapp.ui.components.organisms.UrgencyDropdown
import com.lossabinos.serviceapp.utils.createImageFile
import com.lossabinos.serviceapp.viewmodel.WorkRequestViewModel
import java.io.File


fun launchCamera(
    context: Context,
    launcher: ActivityResultLauncher<Uri>,
    onFileReady: (File, Uri) -> Unit
) {
    val file = createImageFile(context)

    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )

    onFileReady(file, uri)
    launcher.launch(uri)
}

@Composable
fun WorkRequestFormFields(
    errorMessage: String?,
    formErrors: WorkRequestFormErrors,
    formData: WorkRequestUIModel,
    photos: List<WorkRequestPhoto>,
    //viewModel: WorkRequestViewModel = hiltViewModel()
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onFindingsChange: (String) -> Unit,
    onJustificationChange: (String) -> Unit,
    onUrgencyChange: (UrgencyUI) -> Unit,
    onRequiresApprovalChange: (Boolean) -> Unit,
    onPhotoCaptured: (String) -> Unit,
    onPhotoDeleted: (String) -> Unit,
    onCategoryChange: (CategoryUI) -> Unit
) {

    // ───────────────────────────
    // CONTEXTO
    // ───────────────────────────
    val context = LocalContext.current

    // Uri donde se guardará la foto
    var currentPhotoFile by remember { mutableStateOf<File?>(null) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    // ───────────────────────────
    // 📸 LAUNCHER TOMAR FOTO
    // ───────────────────────────
    val takePictureLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) { success ->
            if (success && currentPhotoFile != null) {
                onPhotoCaptured(currentPhotoFile!!.absolutePath)
            }
/*
            if (success && photoUri != null) {
                viewModel.onEvent(
                    WorkRequestUiEvent.OnPhotoCaptured(
                        localPath = File(photoUri!!.path!!).absolutePath
                    )
                )
            }
 */
        }

    // ───────────────────────────
    // 🔐 PERMISO CÁMARA
    // ───────────────────────────
    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                launchCamera(
                    context = context,
                    launcher = takePictureLauncher
                ) { file, uri ->
                    currentPhotoFile = file
                    photoUri = uri
                }
            } else {
                Toast
                    .makeText(context, "Permiso de cámara requerido", Toast.LENGTH_SHORT)
                    .show()
            }
        }



    // 🔴 ERROR DE VALIDACIÓN
    if (!errorMessage.isNullOrEmpty()) {
        Text(
            text = errorMessage,
            color = Color.Red,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }



    // ───────────────────────────
    // UI
    // ───────────────────────────
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        TextInput(
            value = formData.title,
            onValueChange =  onTitleChange,
            placeholder = "Título",
            isError = formErrors.title != null,
            errorText = formErrors.title
        )

        TextInput(
            value = formData.description,
            onValueChange = onDescriptionChange,
            placeholder = "Descripción",
            isError = formErrors.title != null,
            errorText = formErrors.title,
            maxLines = 4,
            maxLength = 500
        )

        TextInput(
            value = formData.findings,
            onValueChange = onFindingsChange,
            placeholder = "Hallazgos",
            isError = formErrors.title != null,
            errorText = formErrors.title
        )

        TextInput(
            value = formData.justification,
            onValueChange = onJustificationChange,
            placeholder = "Justificación",
            isError = formErrors.title != null,
            errorText = formErrors.title
        )

        UrgencyDropdown(
            selected = formData.urgency,
            onUrgencySelected = onUrgencyChange
        )

        CategoryDropdown(
            selected = formData.category,
            onCategorySelected = onCategoryChange
        )

        // ───────────────────────────
        // 📸 BOTÓN TOMAR FOTO
        // ───────────────────────────
        ActionButtonAtom(
            text = "Tomar foto (${photos.size}/3)",
            icon = Icons.Default.CameraAlt,
            onClick = {

                if (photos.size >= 3) return@ActionButtonAtom

                when {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED -> {

                        launchCamera(
                            context,
                            takePictureLauncher
                        ) { file, uri ->
                            currentPhotoFile = file
                            photoUri = uri
                        }
                    }

                    else -> {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            }

            /*
                        text = "Tomar foto (${photos.size}/3)",
                        icon = Icons.Default.CameraAlt,
                        onClick = {

                            if (photos.size >= 3) return@ActionButtonAtom

                            when {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED -> {
                                    launchCamera(context, takePictureLauncher) {
                                        photoUri = it
                                    }
                                }

                                else -> {
                                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            }
                        }
             */
        )

        // ───────────────────────────
        // 🖼️ PREVIEW FOTOS
        // ───────────────────────────
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(photos) { photo ->
                Box {
                    Image(
                        painter = rememberAsyncImagePainter(File(photo.localPath)),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 6.dp, y = (-6).dp)
                            .size(20.dp) // 👈 tamaño REAL
                            .background(
                                color = Color.Black.copy(alpha = 0.6f),
                                shape = CircleShape
                            )
                            .clickable {
                                onPhotoDeleted
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Eliminar",
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }

        CheckboxRow(
            checked = formData.requiresCustomerApproval,
            label = "Requiere aprobación del cliente",
            onCheckedChange = onRequiresApprovalChange
        )
    }
}



/*
@Composable
fun WorkRequestFormFields(
    formData: WorkRequestUIModel,
    photos: List<WorkRequestPhoto>,
    viewModel: WorkRequestViewModel = hiltViewModel()
) {

    // ───────────────────────────
    // SETUP DE CÁMARA (VA AQUÍ)
    // ───────────────────────────
    val context = LocalContext.current

    var photoUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val takePictureLauncher


    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                launchCamera()
            } else {
                Toast
                    .makeText(context, "Permiso de cámara requerido", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    fun launchCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        cameraLauncher.launch(intent)
    }

    val takePictureLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) { success ->
            if (success && photoUri != null) {
                viewModel.onEvent(
                    WorkRequestUiEvent.OnPhotoCaptured(
                        localPath = File(photoUri!!.path!!).absolutePath
                    )
                )
            }
        }

    fun createImageFile(context: Context): File {
        val dir = File(context.filesDir, "work_requests")
        if (!dir.exists()) dir.mkdirs()

        return File(
            dir,
            "WR_${System.currentTimeMillis()}.jpg"
        )
    }


    // ───────────
    //  UI
    // ──────────-
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        TextInput(
            value = formData.title,
            onValueChange = {
                viewModel.onEvent(WorkRequestUiEvent.OnTitleChange(it))
            },
            placeholder = "Título"
        )

        TextInput(
            value = formData.description,
            onValueChange = {
                viewModel.onEvent(WorkRequestUiEvent.OnDescriptionChange(it))
            },
            placeholder = "Descripción"
        )

        TextInput(
            value = formData.findings,
            onValueChange = {
                viewModel.onEvent(WorkRequestUiEvent.OnFindingsChange(it))
            },
            placeholder = "Hallazgos"
        )

        TextInput(
            value = formData.justification,
            onValueChange = {
                viewModel.onEvent(WorkRequestUiEvent.OnJustificationChange(it))
            },
            placeholder = "Justificación"
        )

        // ✅ NUEVO: Dropdown de urgencia
        UrgencyDropdown(
            selected = formData.urgency,
            onUrgencySelected = {
                viewModel.onEvent(WorkRequestUiEvent.OnUrgencyChange(it))
            }
        )

        // ─────────────────────────────────────────────
        // 📸 BOTÓN TOMAR FOTO
        // ─────────────────────────────────────────────
        ActionButtonAtom(
            text = "Tomar foto (${photos.size}/3)",
            icon = Icons.Default.CameraAlt,
            enabled = photos.size < 3,
            onClick = {

                                val file = createImageFile(context)

                                val uri = FileProvider.getUriForFile(
                                    context,
                                    "${context.packageName}.fileprovider",
                                    file
                                )

                                photoUri = uri
                                takePictureLauncher.launch(uri)

            }
        )

        // ─────────────────────────────────────────────
        // 🖼️ PREVIEW DE FOTOS
        // ─────────────────────────────────────────────
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(photos) { photo ->
                Box {
                    Image(
                        painter = rememberAsyncImagePainter(File(photo.localPath)),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    IconButton(
                        onClick = {
                            viewModel.onEvent(
                                WorkRequestUiEvent.OnPhotoDeleted(photo.id)
                            )
                        },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Eliminar"
                        )
                    }
                }
            }
        }

        CheckboxRow(
            checked = formData.requiresCustomerApproval,
            label = "Requiere aprobación del cliente",
            onCheckedChange = {
                viewModel.onEvent(
                    WorkRequestUiEvent.OnRequiresApprovalChange(it)
                )
            }
        )
    }
}
*/

