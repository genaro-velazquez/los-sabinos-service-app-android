package com.lossabinos.data.datasource.local

import com.lossabinos.domain.models.Location
import com.lossabinos.domain.models.LocationEvent
import kotlinx.coroutines.flow.Flow

// This class now depends on a WebSocketClient that will also be in this package
class LocationWebSocketDataSource(
    private val webSocketClient: WebSocketClient
) {
    fun connect() = webSocketClient.connect()
    fun disconnect() = webSocketClient.disconnect()
    fun events(): Flow<LocationEvent> = webSocketClient.events
    fun send(location: Location) = webSocketClient.send(location)
}
