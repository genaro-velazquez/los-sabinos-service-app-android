package com.lossabinos.data.repositories

import android.util.Log
import com.lossabinos.data.datasource.remoto.websocket.WebSocketManager
import com.lossabinos.domain.repositories.WebSocketRepository
import com.lossabinos.domain.valueobjects.LocationEvent
import com.lossabinos.domain.valueobjects.LocationVO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class WebSocketRepositoryImpl(

) : WebSocketRepository {
    private val TAG = "WebSocketRepository"
    private var webSocketManager: WebSocketManager? = null

    private val _messages = MutableSharedFlow<String>(replay = 1)
    override fun observeMessages(): Flow<String> = _messages.asSharedFlow()

    private val _connectionStatus = MutableSharedFlow<Boolean>(replay = 1)
    override fun observeConnectionStatus(): Flow<Boolean> = _connectionStatus.asSharedFlow()

    override fun connect(accessToken: String) {
        Log.d(TAG, "Conectando WebSocket...")

        if (webSocketManager == null) {
            webSocketManager = WebSocketManager(
                accessToken = accessToken,
                onMessageReceived = { message ->
                    _messages.tryEmit(message)
                    Log.d(TAG, "Mensaje emitido: $message")
                },
                onConnectionStatusChanged = { isConnected ->
                    _connectionStatus.tryEmit(isConnected)
                    Log.d(TAG, "Estado de conexión: $isConnected")
                }
            )
        }
        webSocketManager?.connect()
    }

    override fun disconnect() {
        Log.d(TAG, "Desconectando WebSocket...")
        webSocketManager?.disconnect()
        webSocketManager = null
    }

    override fun sendMessage(message: String) {
        webSocketManager?.sendMessage(message)
    }

    override fun isConnected(): Boolean {
        return webSocketManager?.isConnected() ?: false
    }
}
