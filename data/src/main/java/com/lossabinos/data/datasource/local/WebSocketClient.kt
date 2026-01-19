package com.lossabinos.data.datasource.local

import android.util.Log
import androidx.work.WorkManager
import com.lossabinos.data.datasource.local.database.dao.LocationDao
import com.lossabinos.data.datasource.local.database.entities.LocationEntity
import com.lossabinos.data.utilities.NetworkStateMonitor
import com.lossabinos.domain.models.Location
import com.lossabinos.domain.models.LocationData
import com.lossabinos.domain.models.LocationEvent
import com.lossabinos.domain.repositories.UserPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class WebSocketClient @Inject constructor(
    private val tokenManager: TokenManager,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val workManager: WorkManager,
    private val locationDao: LocationDao,
    private val offlineLocationQueueProvider: Provider<OfflineLocationQueue>,
    private val networkStateMonitor: NetworkStateMonitor,
    private val scope: CoroutineScope
) {
    private val offlineLocationQueue by lazy { offlineLocationQueueProvider.get() }

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(0, TimeUnit.SECONDS)
        .build()
        
    private var webSocket: WebSocket? = null
    private var heartbeatJob: Job? = null
    private var lastPongTime: Long = 0

    private val _events = MutableSharedFlow<LocationEvent>()
    val events: Flow<LocationEvent> = _events.asSharedFlow()

    private val json = Json {
        ignoreUnknownKeys = true
        classDiscriminator = "type"
    }

    init {
        networkStateMonitor.isOnline
            .onEach { isOnline ->
                if (isOnline) {
                    Log.d("WebSocketClient", "Network is back online. Checking connection...")
                    if (webSocket == null) {
                        connect()
                    }
                    scope.launch(Dispatchers.IO) {
                        offlineLocationQueue.processQueue()
                    }
                }
            }
            .launchIn(scope)
    }

    fun connect() {
        if (webSocket != null) return

        val token = tokenManager.getAccessToken()
        
        if (token.isBlank()) {
            Log.e("WebSocketClient", "Cannot connect: auth token is missing.")
            return
        }

        val url = "wss://lossabinos-e9gvbjfrf9h5dphf.eastus2-01.azurewebsites.net/api/v1/maintenance-alerts/ws/notifications?token=$token"
        
        Log.d("WebSocketClient", "Connecting to production URL: $url")

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $token")
            .build()

        webSocket = client.newWebSocket(request, LocationWebSocketListener())
    }

    private fun startHeartbeat() {
        heartbeatJob?.cancel()
        lastPongTime = System.currentTimeMillis()
        heartbeatJob = scope.launch(Dispatchers.IO) {
            while (isActive) {
                delay(30000) // Enviar ping cada 30 segundos según la guía
                if (webSocket != null) {
                    Log.d("WebSocketClient", "📤 Enviando PING manual al servidor...")
                    val sent = webSocket?.send("{\"type\": \"ping\"}")
                    
                    if (sent == true) {
                        // Esperar 10 segundos para validar si llegó el pong
                        delay(10000)
                        val timeSinceLastPong = System.currentTimeMillis() - lastPongTime
                        if (timeSinceLastPong > 40000) { // 30s + 10s de margen
                            Log.e("WebSocketClient", "❌ Conexión muerta (timeout de PONG). Reconectando...")
                            reconnect()
                            break
                        }
                    }
                }
            }
        }
    }

    private fun reconnect() {
        disconnect()
        scope.launch {
            delay(5000) // Esperar 5 segundos antes de reintentar según la guía
            Log.d("WebSocketClient", "🔄 Intentando reconectar automáticamente...")
            connect()
        }
    }

    fun disconnect() {
        heartbeatJob?.cancel()
        heartbeatJob = null
        webSocket?.close(1000, "User disconnected")
        webSocket = null
    }

    fun send(location: Location) {
        val jsonMessage = json.encodeToString(location)
        val sent = webSocket?.send(jsonMessage)
        if (sent != true) {
            scope.launch(Dispatchers.IO) {
                offlineLocationQueue.enqueue(location)
            }
        }
    }

    private fun LocationData.toEntity() = LocationEntity(
        id = UUID.randomUUID().toString(),
        userId = this.userId,
        latitude = this.latitude,
        longitude = this.longitude,
        timestamp = this.timestamp
    )

    private inner class LocationWebSocketListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d("WebSocketClient", "✅ CONEXIÓN ESTABLECIDA CON ÉXITO A AZURE")
            lastPongTime = System.currentTimeMillis()
            startHeartbeat()
            _events.tryEmit(LocationEvent.Connected("unknown", "unknown"))
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("WebSocketClient", "📩 MENSAJE RECIBIDO: $text")
            
            try {
                val event = json.decodeFromString<LocationEvent>(text)
                
                if (event is LocationEvent.Pong) {
                    Log.d("WebSocketClient", "💓 PONG recibido del servidor")
                    lastPongTime = System.currentTimeMillis()
                }
                
                _events.tryEmit(event)
            } catch (e: Exception) {
                // Manejo manual por si el JSON de pong es muy simple
                if (text.contains("\"type\":\"pong\"") || text.contains("\"type\": \"pong\"")) {
                    Log.d("WebSocketClient", "💓 PONG (manual) recibido")
                    lastPongTime = System.currentTimeMillis()
                } else {
                    Log.e("WebSocketClient", "❌ Error al procesar mensaje: $text", e)
                }
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.e("WebSocketClient", "❌ ERROR DE CONEXIÓN AL WEBSOCKET: ${t.message}")
            reconnect()
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("WebSocketClient", "ℹ️ CONEXIÓN CERRADA: $reason (Código: $code)")
            // Si el cierre no fue normal (1000), intentamos reconectar
            if (code != 1000) {
                reconnect()
            }
        }
    }
}
