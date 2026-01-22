package com.lossabinos.serviceapp.ui.components.templates

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lossabinos.domain.enums.NotificationPriority
import com.lossabinos.serviceapp.ui.components.atoms.NotificationBadgeAtom
import com.lossabinos.serviceapp.ui.components.molecules.NotificationItemUIModel
import com.lossabinos.serviceapp.ui.components.organisms.NotificationListOrganism
import androidx.compose.ui.tooling.preview.Preview
import com.lossabinos.domain.enums.NotificationCategory
import com.lossabinos.domain.enums.NotificationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsTemplate(
    notifications: List<NotificationItemUIModel>,
    unreadCount: Int = 0,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onRefresh: () -> Unit = {},
    onNotificationClick: (NotificationItemUIModel) -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onDismissError: () -> Unit = {},
    onReachedEnd: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            println("🔴 ERROR MESSAGE: $errorMessage")  // ← AGREGAR LOG
            snackbarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short // 4 segundos automáticos
            )
            onDismissError()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Notificaciones",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            if (unreadCount > 0) {
                                NotificationBadgeAtom(count = unreadCount)
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Atrás"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            NotificationListOrganism(
                notifications = notifications,
                isLoading = isLoading,
                onRefresh = onRefresh,
                onNotificationClick = onNotificationClick,
                onReachedEnd = onReachedEnd,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        }

        // Snackbar en la PARTE SUPERIOR - FUERA del Scaffold
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            snackbar = { snackbarData ->
                Snackbar(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = Color(0xFFD32F2F),
                    contentColor = Color.White
                ) {
                    Text(
                        text = snackbarData.visuals.message.ifEmpty { "Error desconocido" },
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_5")
@Composable
fun NotificationsTemplatePreview() {
    MaterialTheme {
        NotificationsTemplate(
            notifications = listOf(
                NotificationItemUIModel(
                    id = "1",
                    title = "Servicio Completado",
                    message = "El servicio de mantenimiento ha sido completado exitosamente",
                    priority = NotificationPriority.LOW,
                    timestamp = System.currentTimeMillis(),
                    isRead = true,
                    type = NotificationType.NEW_NOTIFICATION,
                    category = NotificationCategory.USER
                ),
                NotificationItemUIModel(
                    id = "2",
                    title = "Falta de Documentación",
                    message = "Falta subir evidencia de fotos para este servicio",
                    priority = NotificationPriority.HIGH,
                    timestamp = System.currentTimeMillis() - 3600000,
                    isRead = false,
                    type = NotificationType.NEW_NOTIFICATION,
                    category = NotificationCategory.USER
                ),
                NotificationItemUIModel(
                    id = "3",
                    title = "Nuevo Servicio Asignado",
                    message = "Se te ha asignado un nuevo servicio de mantenimiento",
                    priority = NotificationPriority.NORMAL,
                    timestamp = System.currentTimeMillis() - 7200000,
                    isRead = false,
                    type = NotificationType.NEW_NOTIFICATION,
                    category = NotificationCategory.USER
                ),
                NotificationItemUIModel(
                    id = "4",
                    title = "Error Crítico",
                    message = "Problema crítico detectado en el sistema de sincronización",
                    priority = NotificationPriority.HIGH,
                    timestamp = System.currentTimeMillis() - 10800000,
                    isRead = false,
                    type = NotificationType.NEW_NOTIFICATION,
                    category = NotificationCategory.USER
                )
            ),
            unreadCount = 3,
            isLoading = false,
            errorMessage = null,
            onRefresh = {},
            onNotificationClick = {},
            onNavigateBack = {},
            onDismissError = {}
        )
    }
}