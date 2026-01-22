package com.lossabinos.serviceapp.screens.notifications

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lossabinos.domain.enums.NotificationCategory
import com.lossabinos.domain.enums.NotificationPriority
import com.lossabinos.domain.enums.NotificationType
import com.lossabinos.serviceapp.ui.components.molecules.NotificationItemUIModel
import com.lossabinos.serviceapp.ui.components.templates.NotificationsTemplate
import com.lossabinos.serviceapp.viewmodel.NotificationsEvent
import com.lossabinos.serviceapp.viewmodel.NotificationsViewModel

@Composable
fun NotificationsScreen(
    onNavigateBack: () -> Unit = {},
    notificationsViewModel: NotificationsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    // ==========================================
    // 1️⃣ OBSERVAR DATOS DEL VIEWMODEL
    // ==========================================
    val state = notificationsViewModel.state.collectAsStateWithLifecycle().value

    // ==========================================
    // 2️⃣ CARGAR DATOS AL ABRIR PANTALLA
    // ==========================================
    LaunchedEffect(Unit) {
        notificationsViewModel.onEvent(NotificationsEvent.LoadNotifications)
    }

    // ==========================================
    // 3️⃣ RENDERIZAR TEMPLATE
    // ==========================================
    NotificationsTemplate(
        notifications = state.notifications,
        unreadCount = state.unreadCount,
        isLoading = state.isLoading,
        errorMessage = state.errorMessage,
        onRefresh = {
            notificationsViewModel.onEvent(NotificationsEvent.RefreshNotifications)
        },
        onNotificationClick = { notification ->
            notificationsViewModel.onEvent(
                NotificationsEvent.NotificationClicked(notification)
            )
        },
        onNavigateBack = onNavigateBack,
        onDismissError = {
            notificationsViewModel.onEvent(NotificationsEvent.DismissError)
        },
        onReachedEnd = {
            notificationsViewModel.onEvent(NotificationsEvent.LoadMoreNotifications)
        },
        modifier = modifier
    )
}

@Preview(showBackground = true, device = "id:pixel_5")
@Composable
fun NotificationsScreenPreview() {
    MaterialTheme {
        // Llamar directamente sin ViewModel
        NotificationsTemplatePreview()
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
                    category = NotificationCategory.SYSTEM,
                    actionUrl = null
                ),
                NotificationItemUIModel(
                    id = "2",
                    title = "Falta de Documentación",
                    message = "Falta subir evidencia de fotos para este servicio",
                    priority = NotificationPriority.HIGH,
                    timestamp = System.currentTimeMillis() - 3600000,
                    isRead = false,
                    type = NotificationType.NEW_NOTIFICATION,
                    category = NotificationCategory.USER,
                    actionUrl = null
                )
            ),
            unreadCount = 1,
            isLoading = false,
            errorMessage = null,
            onRefresh = {},
            onNotificationClick = {},
            onNavigateBack = {},
            onDismissError = {},
            onReachedEnd = {}
        )
    }
}