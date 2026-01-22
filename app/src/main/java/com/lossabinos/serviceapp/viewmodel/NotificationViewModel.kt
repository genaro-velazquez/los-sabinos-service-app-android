package com.lossabinos.serviceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lossabinos.domain.usecases.notifications.GetNotificationsUseCase
import com.lossabinos.serviceapp.mappers.NotificationMapper.toUIModels
import com.lossabinos.serviceapp.ui.components.molecules.NotificationItemUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.update




// presentation/ui/screens/notifications/NotificationsState.kt

data class NotificationsState(
    val notifications: List<NotificationItemUIModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val unreadCount: Int = 0,
    val currentPage: Int = 0,
    val hasMorePages: Boolean = true,
    val isRefreshing: Boolean = false
)

sealed class NotificationsEvent {
    object LoadNotifications : NotificationsEvent()
    object RefreshNotifications : NotificationsEvent()
    object LoadMoreNotifications : NotificationsEvent()
    data class NotificationClicked(val notification: NotificationItemUIModel) : NotificationsEvent()
    object DismissError : NotificationsEvent()
    object ResetPagination : NotificationsEvent()
}

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val getNotificationsUseCase: GetNotificationsUseCase
) : ViewModel() {

    // ==========================================
    // CONFIGURACIÓN
    // ==========================================
    private val pageSize = 10 // ← CONFIGURABLE FIJA

    // ==========================================
    // STATE
    // ==========================================
    private val _state = MutableStateFlow(NotificationsState())
    val state: StateFlow<NotificationsState> = _state.asStateFlow()

    // ==========================================
    // MÉTODO PRINCIPAL - DISTRIBUIDOR DE EVENTOS
    // ==========================================
    fun onEvent(event: NotificationsEvent) {
        when (event) {
            is NotificationsEvent.LoadNotifications -> loadNotifications()
            is NotificationsEvent.RefreshNotifications -> refreshNotifications()
            is NotificationsEvent.LoadMoreNotifications -> loadMoreNotifications()
            is NotificationsEvent.NotificationClicked -> handleNotificationClick(event.notification)
            is NotificationsEvent.DismissError -> dismissError()
            is NotificationsEvent.ResetPagination -> resetPagination()
        }
    }

    // ==========================================
    // 1️⃣ CARGAR NOTIFICACIONES INICIAL
    // ==========================================
    private fun loadNotifications() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val response = getNotificationsUseCase(
                    page = 1,
                    pageSize = pageSize
                )

                val uiModels = response.notifications.toUIModels()

                _state.update {
                    it.copy(
                        notifications = uiModels,
                        isLoading = false,
                        errorMessage = null,
                        currentPage = 0,
                        hasMorePages = (response.page + 1) < (response.total / pageSize),
                        unreadCount = response.unreadCount
                    )
                }
            } catch (e: Exception) {
                println("🔴 EXCEPTION: ${e.message}")
                println("🔴 CAUSE: ${e.cause}")
                println("🔴 STACKTRACE: ${e.stackTraceToString()}")

                val errorMessage = e.message?.takeIf { it.isNotEmpty() }
                    ?: e.cause?.message?.takeIf { it.isNotEmpty() }
                    ?: "Error al cargar notificaciones"


                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = errorMessage
                    )
                }
            }
        }
    }

    // ==========================================
    // 2️⃣ REFRESCAR NOTIFICACIONES
    // ==========================================
    private fun refreshNotifications() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }

            try {
                val response = getNotificationsUseCase(
                    page = 1,
                    pageSize = pageSize
                )

                val uiModels = response.notifications.toUIModels()

                _state.update {
                    it.copy(
                        notifications = uiModels,
                        isRefreshing = false,
                        errorMessage = null,
                        currentPage = 0,
                        hasMorePages = (response.page + 1) < (response.total / pageSize),
                        unreadCount = response.unreadCount
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isRefreshing = false,
                        errorMessage = e.message ?: "Error al refrescar notificaciones"
                    )
                }
            }
        }
    }

    // ==========================================
    // 3️⃣ CARGAR MÁS (PAGINACIÓN INFINITA)
    // ==========================================
    private fun loadMoreNotifications() {
        val currentState = _state.value

        // No cargar si ya está cargando o no hay más páginas
        if (currentState.isLoading || !currentState.hasMorePages) {
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val nextPage = currentState.currentPage + 1
                val response = getNotificationsUseCase(
                    page = nextPage,
                    pageSize = pageSize
                )

                val newNotifications = response.notifications.toUIModels()

                _state.update {
                    it.copy(
                        notifications = it.notifications + newNotifications,
                        isLoading = false,
                        errorMessage = null,
                        currentPage = nextPage,
                        hasMorePages = (response.page + 1) < (response.total / pageSize),
                        unreadCount = response.unreadCount
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error al cargar más notificaciones"
                    )
                }
            }
        }
    }

    // ==========================================
    // 4️⃣ MARCAR COMO LEÍDA AL HACER CLICK
    // ==========================================
    private fun handleNotificationClick(notification: NotificationItemUIModel) {
        // Actualizar el estado local marcando como leída
        _state.update { currentState ->
            val updatedNotifications = currentState.notifications.map {
                if (it.id == notification.id) {
                    it.copy(isRead = true)
                } else {
                    it
                }
            }

            currentState.copy(
                notifications = updatedNotifications,
                unreadCount = (currentState.unreadCount - 1).coerceAtLeast(0)
            )
        }

        // ❓ AQUÍ: ¿Llamar a API para marcar como leída en el servidor?
        // Por ahora solo actualiza localmente
    }

    // ==========================================
    // 5️⃣ DESCARTAR ERROR
    // ==========================================
    private fun dismissError() {
        _state.update { it.copy(errorMessage = null) }
    }

    // ==========================================
    // 6️⃣ RESETEAR PAGINACIÓN
    // ==========================================
    private fun resetPagination() {
        _state.update {
            it.copy(
                currentPage = 1,
                hasMorePages = true,
                notifications = emptyList()
            )
        }
    }
}