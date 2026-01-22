package com.lossabinos.data.datasource.remoto.websocket

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import android.util.Log
import java.util.concurrent.TimeUnit
import okhttp3.Response
import org.json.JSONObject

class WebSocketManager(
    private val accessToken: String,
    private val onMessageReceived: (String) -> Unit,
    private val onConnectionStatusChanged: (Boolean) -> Unit
) {
    private val TAG = "WebSocketManager"

    // Configurar OkHttpClient
    private val client = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    private var webSocket: WebSocket? = null
    private var reconnectAttempts = 0
    private val maxReconnectAttempts = 10

    // URLs según entorno
    private val baseUrl = "wss://lossabinos-e9gvbjfrf9h5dphf.eastus2-01.azurewebsites.net"  // Desarrollo (emulador)
    // private val baseUrl = "wss://api.tudominio.com"  // Producción

    private val wsUrl = "$baseUrl/api/v1/maintenance-alerts/ws/notifications?token=$accessToken"

    // WebSocket Listener
    private val webSocketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d(TAG, "✅ WebSocket conectado exitosamente")
            onConnectionStatusChanged(true)
            reconnectAttempts = 0
            startHeartbeat()
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d(TAG, "📨 Mensaje recibido: $text")
            onMessageReceived(text)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            Log.d(TAG, "📨 Mensaje binario recibido")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d(TAG, "🔴 WebSocket cerrando: código=$code, razón=$reason")
            webSocket.close(1000, null)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d(TAG, "🔴 WebSocket cerrado: código=$code")
            onConnectionStatusChanged(false)

            // Reconectar si no fue cierre intencional (código 1000 = OK)
            if (code != 1000) {
                reconnect()
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.e(TAG, "❌ Error en WebSocket: ${t.message}")
            t.printStackTrace()
            onConnectionStatusChanged(false)
            reconnect()
        }
    }

    // Conectar
    fun connect() {
        try {
            val request = Request.Builder()
                .url(wsUrl)
                .build()

            webSocket = client.newWebSocket(request, webSocketListener)
            Log.d(TAG, "🔌 Intentando conectar a: $wsUrl")
        } catch (e: Exception) {
            Log.e(TAG, "Error al conectar: ${e.message}")
        }
    }

    // Desconectar
    fun disconnect() {
        try {
            webSocket?.close(1000, "Cierre intencional")
            webSocket = null
            stopHeartbeat()
            Log.d(TAG, "Desconexión exitosa")
        } catch (e: Exception) {
            Log.e(TAG, "Error al desconectar: ${e.message}")
        }
    }

    // Enviar mensaje
    fun sendMessage(message: String) {
        try {
            webSocket?.send(message) ?: Log.w(TAG, "WebSocket no conectado")
        } catch (e: Exception) {
            Log.e(TAG, "Error al enviar mensaje: ${e.message}")
        }
    }

    // Reconectar con backoff exponencial
    private fun reconnect() {
        if (reconnectAttempts < maxReconnectAttempts) {
            reconnectAttempts++
            val delay = minOf(1000L * (1L shl reconnectAttempts), 30000L)

            Log.d(TAG, "🔄 Reconectando en ${delay}ms (intento $reconnectAttempts/$maxReconnectAttempts)")

            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                connect()
            }, delay)
        } else {
            Log.e(TAG, "❌ Máximo de intentos de reconexión alcanzado")
        }
    }

    // ========== HEARTBEAT ==========
    private var heartbeatRunnable: Runnable? = null
    private val heartbeatHandler = android.os.Handler(android.os.Looper.getMainLooper())

    private fun startHeartbeat() {
        stopHeartbeat() // Detener el anterior si existe

        heartbeatRunnable = object : Runnable {
            override fun run() {
                if (webSocket != null) {
                    try {
                        // ✅ USAR JSONObject EN LUGAR DE GSON
                        val pingMessage = JSONObject()
                        pingMessage.put("type", "ping")
                        sendMessage(pingMessage.toString())
                        Log.d(TAG, "💓 Ping enviado")
                    } catch (e: Exception) {
                        Log.e(TAG, "Error al enviar ping: ${e.message}")
                    }
                }
                // Programar siguiente ping en 30 segundos
                heartbeatHandler.postDelayed(this, 30000)
            }
        }
        heartbeatHandler.post(heartbeatRunnable!!)
    }

    private fun stopHeartbeat() {
        heartbeatRunnable?.let {
            heartbeatHandler.removeCallbacks(it)
            Log.d(TAG, "Heartbeat detenido")
        }
    }

    // Obtener estado de conexión
    fun isConnected(): Boolean = webSocket != null
}
