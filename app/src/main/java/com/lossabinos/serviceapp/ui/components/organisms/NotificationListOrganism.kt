package com.lossabinos.serviceapp.ui.components.organisms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.lossabinos.domain.enums.NotificationPriority
import com.lossabinos.serviceapp.ui.components.atoms.NotificationEmptyStateAtom
import com.lossabinos.serviceapp.ui.components.molecules.NotificationItemMolecule
import com.lossabinos.serviceapp.ui.components.molecules.NotificationItemUIModel
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import com.lossabinos.domain.enums.NotificationCategory
import com.lossabinos.domain.enums.NotificationType


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NotificationListOrganism(
    notifications: List<NotificationItemUIModel>,
    isLoading: Boolean = false,
    onRefresh: () -> Unit = {},
    onNotificationClick: (NotificationItemUIModel) -> Unit = {},
    onReachedEnd: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()

    // ==========================================
    // DETECTAR CUANDO LLEGA AL FINAL
    // ==========================================
    LaunchedEffect(lazyListState.isScrollInProgress) {
        val lastVisibleIndex = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
        val totalItems = notifications.size

        // Si el último item visible es muy cercano al final, cargar más
        if (lastVisibleIndex >= totalItems - 3 && totalItems > 0 && !isLoading) {
            onReachedEnd()
        }
    }

    LazyColumn(
        state = lazyListState,
        modifier = modifier.fillMaxSize()
    ) {
        // Si no hay notificaciones y no está cargando, mostrar empty state
        if (notifications.isEmpty() && !isLoading) {
            item {
                NotificationEmptyStateAtom(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                )
            }
        }
        // Si hay notificaciones, mostrarlas
        else {
            items(notifications.size) { index ->
                val notification = notifications[index]

                Column {
                    NotificationItemMolecule(
                        notification = notification,
                        onClick = {
                            onNotificationClick(notification)
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )

                    // Divisor entre notificaciones (excepto en la última)
                    if (index < notifications.size - 1) {
                        Divider(
                            color = Color(0xFFE0E0E0),
                            thickness = 1.dp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationListOrganismPreview() {
    MaterialTheme {
        NotificationListOrganism(
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
                    isRead = true,
                    type = NotificationType.NEW_NOTIFICATION,
                    category = NotificationCategory.USER
                )
            ),
            isLoading = false,
            onRefresh = {},
            onNotificationClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationListOrganismEmptyPreview() {
    MaterialTheme {
        NotificationListOrganism(
            notifications = emptyList(), // ← Lista vacía
            isLoading = false,
            onRefresh = {},
            onNotificationClick = {}
        )
    }
}