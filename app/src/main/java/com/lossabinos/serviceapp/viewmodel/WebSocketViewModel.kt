package com.lossabinos.serviceapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lossabinos.domain.usecases.websocket.ConnectWebSocketUseCase
import com.lossabinos.domain.usecases.websocket.DisconnectWebSocketUseCase
import com.lossabinos.domain.usecases.websocket.ObserveWebSocketMessagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject


@HiltViewModel
class WebSocketViewModel @Inject constructor(
    private val connectWebSocketUseCase: ConnectWebSocketUseCase,
    private val disconnectWebSocketUseCase: DisconnectWebSocketUseCase,
    private val observeWebSocketMessagesUseCase: ObserveWebSocketMessagesUseCase
) : ViewModel() {

    private val TAG = "WebSocketViewModel"

    // ========== ESTADOS ==========
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val _messages = MutableStateFlow<List<String>>(emptyList())
    val messages: StateFlow<List<String>> = _messages.asStateFlow()

    private val _lastNotification = MutableStateFlow<String?>(null)
    val lastNotification: StateFlow<String?> = _lastNotification.asStateFlow()

    private val _lastAlert = MutableStateFlow<String?>(null)
    val lastAlert: StateFlow<String?> = _lastAlert.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        observeMessages()
    }

    // ========== CONECTAR ==========
    fun connect(accessToken: String) {
        Log.d(TAG, "🔌 Conectando WebSocket...")
        connectWebSocketUseCase(accessToken)
    }

    // ========== DESCONECTAR ==========
    fun disconnect() {
        Log.d(TAG, "🔴 Desconectando WebSocket...")
        disconnectWebSocketUseCase()
        _isConnected.value = false
    }

    // ========== OBSERVAR MENSAJES ==========
    private fun observeMessages() {
        viewModelScope.launch {
            observeWebSocketMessagesUseCase().collect { messageJson ->
                try {
                    val json = JSONObject(messageJson)
                    val type = json.optString("type", "unknown")

                    Log.d(TAG, "📨 Mensaje recibido: type=$type")

                    when (type) {
                        "connected" -> {
                            handleConnected(json)
                        }
                        "new_notification" -> {
                            handleNewNotification(json)
                        }
                        "maintenance_alert" -> {
                            handleMaintenanceAlert(json)
                        }
                        "pong" -> {
                            Log.d(TAG, "💓 Pong recibido")
                        }
                        "error" -> {
                            handleError(json)
                        }
                        else -> {
                            Log.d(TAG, "Tipo de mensaje desconocido: $type")
                        }
                    }

                    // Guardar en lista de mensajes
                    _messages.value = _messages.value + messageJson

                } catch (e: Exception) {
                    Log.e(TAG, "Error parseando mensaje: ${e.message}")
                    _errorMessage.value = "Error al procesar mensaje: ${e.message}"
                }
            }
        }
    }

    // ========== MANEJAR CONEXIÓN ==========
    private fun handleConnected(json: JSONObject) {
        try {
            val message = json.optString("message", "")
            val userId = json.optString("user_id", "")
            val tenantId = json.optString("tenant_id", "")

            Log.d(TAG, "✅ Conectado: userId=$userId, tenantId=$tenantId")
            _isConnected.value = true
            _errorMessage.value = null

        } catch (e: Exception) {
            Log.e(TAG, "Error procesando conexión: ${e.message}")
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
            val category = notification?.optString("category", "") ?: ""

            val notificationText = "[$priority] $title: $message"
            _lastNotification.value = notificationText

            Log.d(TAG, "📬 Nueva notificación: $notificationText (categoria: $category)")

        } catch (e: Exception) {
            Log.e(TAG, "Error procesando notificación: ${e.message}")
            _errorMessage.value = "Error al procesar notificación"
        }
    }

    // ========== MANEJAR ALERTA DE MANTENIMIENTO ==========
    private fun handleMaintenanceAlert(json: JSONObject) {
        try {
            val data = json.optJSONObject("data")

            val id = data?.optString("id", "") ?: ""
            val title = data?.optString("title", "Alerta de mantenimiento") ?: "Alerta de mantenimiento"
            val message = data?.optString("message", "") ?: ""
            val severity = data?.optString("severity", "normal") ?: "normal"
            val kmUntilDue = data?.optInt("km_until_due", 0) ?: 0

            val alertText = "[$severity] $title: $message (KM: $kmUntilDue)"
            _lastAlert.value = alertText

            Log.d(TAG, "⚠️ Alerta de mantenimiento: $alertText")

        } catch (e: Exception) {
            Log.e(TAG, "Error procesando alerta: ${e.message}")
            _errorMessage.value = "Error al procesar alerta"
        }
    }

    // ========== MANEJAR ERROR ==========
    private fun handleError(json: JSONObject) {
        try {
            val errorMsg = json.optString("message", "Error desconocido")
            Log.e(TAG, "❌ Error: $errorMsg")
            _errorMessage.value = errorMsg

        } catch (e: Exception) {
            Log.e(TAG, "Error procesando error: ${e.message}")
        }
    }

    // ========== LIMPIAR ERRORES ==========
    fun clearError() {
        _errorMessage.value = null
    }

    fun clearNotification() {
        _lastNotification.value = null
    }

    fun clearAlert() {
        _lastAlert.value = null
    }
}
