// presentation/viewmodel/HomeViewModel.kt
package com.lossabinos.serviceapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lossabinos.domain.repositories.HomeSyncStatusRepository
import com.lossabinos.domain.responses.SyncResult
import com.lossabinos.domain.usecases.LocalData.ClearAllUseCase
import com.lossabinos.domain.usecases.authentication.GetAccessTokenUseCase
import com.lossabinos.domain.usecases.checklist.SyncAndSignChecklistUseCase
import com.lossabinos.domain.usecases.checklist.SyncChecklistUseCase
import com.lossabinos.domain.usecases.preferences.GetUserPreferencesUseCase
import com.lossabinos.domain.usecases.websocket.ConnectWebSocketUseCase
import com.lossabinos.domain.usecases.websocket.DisconnectWebSocketUseCase
import com.lossabinos.domain.usecases.websocket.ObserveWebSocketMessagesUseCase
import com.lossabinos.serviceapp.models.ui.HomeSyncStatusUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.lossabinos.serviceapp.navigation.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

/**
 * Estado del Home
 */
data class HomeState(
    val showLogoutDialog: Boolean = false,
    val userName: String = "Cargando...",
    val userLocation: String = "Mexico City",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * Eventos del Home
 */
sealed class HomeEvent {
    object LogoutClicked : HomeEvent()
    object ConfirmLogout : HomeEvent()
    object CancelLogout : HomeEvent()
    data class CompleteServiceClicked(val serviceId: String) : HomeEvent()
    data class SyncServiceClicked(val serviceId: String) : HomeEvent()
    object NavigateToNotificationsClicked : HomeEvent()

}

/**
 * ViewModel para la pantalla Home
 *
 * Responsabilidades:
 * - Manejar estado del Home
 * - Cargar datos del usuario (nombre, ubicación)
 * - Mostrar/ocultar diálogos de confirmación
 * - Emitir eventos de navegación
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserPreferencesUseCase: GetUserPreferencesUseCase,
    private val clearAllUseCase: ClearAllUseCase,
    private val syncAndSignChecklistUseCase: SyncAndSignChecklistUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val connectWebSocketUseCase: ConnectWebSocketUseCase,
    private val disconnectWebSocketUseCase: DisconnectWebSocketUseCase,
    private val observeWebSocketMessagesUseCase: ObserveWebSocketMessagesUseCase,
    private val homeSyncStatusRepository: HomeSyncStatusRepository
) : ViewModel() {

    // ========== ESTADOS PARA WEBSOCKET ==========
    private val _isWebSocketConnected = MutableStateFlow(false)
    val isWebSocketConnected: StateFlow<Boolean> = _isWebSocketConnected.asStateFlow()

    val syncStatusUiState: StateFlow<HomeSyncStatusUiState> =
        homeSyncStatusRepository.lastServicesRefreshAt
            .map { instant ->
                HomeSyncStatusUiState(lastSyncAt = instant)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = HomeSyncStatusUiState()
            )

    val homeHeaderUiState: StateFlow<HomeSyncStatusUiState> =
        combine(
            syncStatusUiState,
            isWebSocketConnected
        ) { syncState, isOnline ->
            syncState.copy(isOnline = isOnline)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeSyncStatusUiState()
        )


    private val _webSocketNotification = MutableStateFlow<String?>(null)
    val webSocketNotification: StateFlow<String?> = _webSocketNotification.asStateFlow()

    private val _webSocketAlert = MutableStateFlow<String?>(null)
    val webSocketAlert: StateFlow<String?> = _webSocketAlert.asStateFlow()

    private val _webSocketError = MutableStateFlow<String?>(null)
    val webSocketError: StateFlow<String?> = _webSocketError.asStateFlow()

    // =======================================

    init {
        connectToWebSocket()
    }

    // ========== CONECTAR A WEBSOCKET ==========
    private fun connectToWebSocket() {
        viewModelScope.launch {
            try {
                val token = getAccessTokenUseCase.execute()
                if (token.isNotEmpty()) {
                    connectWebSocketUseCase(token)
                    observeWebSocketMessages()
                    Log.d("HomeViewModel", "✅ WebSocket conectado")
                } else {
                    Log.e("HomeViewModel", "❌ No hay token")
                    _webSocketError.value = "No se pudo obtener el token de autenticación"
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "❌ Error conectando WebSocket: ${e.message}")
                _webSocketError.value = "Error al conectar: ${e.message}"
            }
        }
    }

    // ========== OBSERVAR MENSAJES WEBSOCKET ==========
    private fun observeWebSocketMessages() {
        viewModelScope.launch {
            observeWebSocketMessagesUseCase().collect { messageJson ->
                Log.d("HomeViewModel", "📨 RAW MESSAGE RECIBIDO: $messageJson")  // ← AGREGAR

                try {
                    val json = JSONObject(messageJson)
                    val type = json.optString("type", "unknown")

                    Log.d("HomeViewModel", "📨 Mensaje WebSocket: $type")

                    when (type) {
                        "connected" -> {
                            _isWebSocketConnected.value = true
                            Log.d("HomeViewModel", "✅ Conectado al WebSocket")
                        }

                        "new_notification" -> {
                            handleNewNotification(json)
                        }

                        "maintenance_alert" -> {
                            handleMaintenanceAlert(json)
                        }

                        "error" -> {
                            val errorMsg = json.optString("message", "Error desconocido")
                            _webSocketError.value = errorMsg
                            Log.e("HomeViewModel", "❌ Error: $errorMsg")

                            if (errorMsg.contains("Token JWT inválido") ||
                                errorMsg.contains("expirado")) {
                                Log.d("HomeViewModel", "🔄 Token expirado, reconectando...")
                                reconnectWithNewToken()
                            }
                        }

                        "pong" -> {
                            Log.d("HomeViewModel", "💓 Pong recibido")
                        }

                        else -> {
                            Log.d("HomeViewModel", "Tipo desconocido: $type")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("HomeViewModel", "Error procesando mensaje: ${e.message}")
                    _webSocketError.value = "Error al procesar mensaje: ${e.message}"
                }
            }
        }
    }

    private fun reconnectWithNewToken() {
        viewModelScope.launch {
            delay(2000)  // Esperar 2 segundos
            Log.d("HomeViewModel", "Intentando reconectar con token nuevo...")
            connectToWebSocket()
        }
    }

    // ========== MANEJAR NUEVA NOTIFICACIÓN ==========
    private fun handleNewNotification(json: JSONObject) {
        try {
            val data = json.optJSONObject("data")
            val notification = data?.optJSONObject("notification")

            val title = notification?.optString("title", "Nueva notificación") ?: "Nueva notificación"
            val message = notification?.optString("message", "") ?: ""
            val priority = notification?.optString("priority", "normal") ?: "normal"

            val notificationText = "[$priority] $title\n\n$message"
            _webSocketNotification.value = notificationText

            Log.d("HomeViewModel", "📬 Notificación: $title")

        } catch (e: Exception) {
            Log.e("HomeViewModel", "Error procesando notificación: ${e.message}")
        }
    }

    // ========== MANEJAR ALERTA DE MANTENIMIENTO ==========
    private fun handleMaintenanceAlert(json: JSONObject) {
        try {
            val data = json.optJSONObject("data")

            val title = data?.optString("title", "Alerta de mantenimiento") ?: "Alerta de mantenimiento"
            val message = data?.optString("message", "") ?: ""
            val severity = data?.optString("severity", "normal") ?: "normal"
            val kmUntilDue = data?.optInt("km_until_due", 0) ?: 0

            val alertText = "[$severity] $title\n\n$message\n\nKM: $kmUntilDue"
            _webSocketAlert.value = alertText

            Log.d("HomeViewModel", "⚠️ Alerta: $title")

        } catch (e: Exception) {
            Log.e("HomeViewModel", "Error procesando alerta: ${e.message}")
        }
    }

    // ========== LIMPIAR MENSAJES ==========
    fun clearWebSocketNotification() {
        _webSocketNotification.value = null
    }

    fun clearWebSocketAlert() {
        _webSocketAlert.value = null
    }

    fun clearWebSocketError() {
        _webSocketError.value = null
    }

    // ========== DESCONECTAR AL DESTRUIR ==========
    override fun onCleared() {
        super.onCleared()
        disconnectWebSocketUseCase()
        Log.d("HomeViewModel", "WebSocket desconectado")
    }


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun clearError() {
        _errorMessage.value = null
    }


    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _navigationEvent = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvent: StateFlow<NavigationEvent?> = _navigationEvent.asStateFlow()

    init {
        // Cargar datos del usuario al inicializar el ViewModel
        loadUserPreferences()
    }

    /**
     * Carga las preferencias del usuario desde el UseCase
     */
    private fun loadUserPreferences() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }

                // ✅ Ejecutar el UseCase
                val fullName = "${getUserPreferencesUseCase.getUserFirstName()}  ${getUserPreferencesUseCase.getUserLastName()}"

                // ✅ Actualizar estado con datos del usuario
                _state.update {
                    it.copy(
                        userName = fullName,  // ← Nombre real del usuario
                        userLocation = "Mexico City", //?: "Mexico City",
                        isLoading = false
                    )
                }

            } catch (e: Exception) {
                // ❌ Error al cargar
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error al cargar datos del usuario"
                    )
                }
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.LogoutClicked -> {
                // Mostrar dialog de confirmación
                _state.update { it.copy(showLogoutDialog = true) }
            }
            is HomeEvent.ConfirmLogout -> {
                // Confirmar logout y navegar a login
//                _state.update { it.copy(showLogoutDialog = false) }
//                _navigationEvent.value = NavigationEvent.NavigateToLogin
                viewModelScope.launch {
                    try {
                        println("🧹 [LOGOUT] Limpiando base de datos local")
                        clearAllUseCase() // ✅ BORRA ROOM
                        println("✅ [LOGOUT] Datos locales eliminados")
                        _state.update { it.copy(showLogoutDialog = false) }
                        _navigationEvent.value = NavigationEvent.NavigateToLogin

                    } catch (e: Exception) {
                        println("❌ Error limpiando datos: ${e.message}")
                    }
                }

            }
            is HomeEvent.CancelLogout -> {
                // Cancelar logout y cerrar dialog
                _state.update { it.copy(showLogoutDialog = false) }
            }
            is HomeEvent.CompleteServiceClicked -> {
                println("✅ Navegando a VehicleRegisterScreen: ${event.serviceId}")
                //_navigationEvent.value = NavigationEvent.NavigateToChecklistProgress(event.serviceId)
                _navigationEvent.value = NavigationEvent.NavigateToVehicleRegistration(event.serviceId)
            }
            is HomeEvent.SyncServiceClicked -> {
                syncService(event.serviceId)
            }
            is HomeEvent.NavigateToNotificationsClicked -> {
                _navigationEvent.value = NavigationEvent.NavigateToNotifications
            }
        }
    }

    fun clearNavigationEvent(){
        _navigationEvent.value = null
    }

    /**
     * Limpia el estado del ViewModel
     */
    fun clearState() {
        _state.value = HomeState()
        _navigationEvent.value = null
    }

    private val _uiMessage = MutableStateFlow<String?>(null)
    val uiMessage = _uiMessage.asStateFlow()

    fun clearMessage() {
        _uiMessage.value = null
    }

    private fun  syncService(serviceId: String) {
        viewModelScope.launch {
            _isLoading.value = true

            //when (val result = syncChecklistUseCase(serviceId)) {
            when (val result = syncAndSignChecklistUseCase(serviceId)) {

                SyncResult.Success -> {
                    println("✅ Sincronización exitosa")
                }

                SyncResult.AlreadySynced -> {
                    _uiMessage.value = "Este servicio ya fue sincronizado."
                }

                is SyncResult.Error -> {
                    _errorMessage.value = result.message
                }
            }

            _isLoading.value = false
        }

        /*
                viewModelScope.launch {
                    try {
                        println("🔄 [HomeVM] Sincronizando: $serviceId")
                        _isLoading.value = true
                        _errorMessage.value = null

                        syncChecklistUseCase(serviceId)

                        println("✅ [HomeVM] Sincronización exitosa")

                    } catch (e: Exception) {
                        println("❌ [HomeVM] CATCH - Error: ${e.message}")
                        _isLoading.value = true
                        _errorMessage.value = e.message ?: "Error desconocido en la sincronización"
                        println("❌ [HomeVM] _errorMessage = ${_errorMessage.value}")
                    }
                }
         */
    }

}

/**
 * NOTAS:
 *
 * ✅ HomePage ahora:
 * - Inyecta HomeViewModel
 * - Observa state con collectAsState()
 * - Obtiene userName del state (nombre real)
 * - Obtiene userLocation del state (ubicación real)
 * - Pasa datos reales a HomeHeaderSection
 *
 * ✅ Flujo:
 * 1. HomePage carga
 * 2. HomeViewModel init → loadUserPreferences()
 * 3. Ejecuta GetUserPreferencesUseCase
 * 4. state.userName se actualiza
 * 5. HomePage observa cambio
 * 6. Re-composición con nombre real
 * 7. Muestra: "Isabella Rodriguez" (o el nombre real)
 *
 * ✅ Ventajas:
 * - Datos reales del usuario
 * - Se carga automáticamente
 * - Reativo (actualiza si cambian datos)
 * - Manejo de errores
 * - Manejo de loading
 */

