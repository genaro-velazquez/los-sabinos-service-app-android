package com.lossabinos.data.local

import com.lossabinos.data.datasource.local.WebSocketClient
import com.lossabinos.domain.models.Location
import com.lossabinos.domain.models.LocationEvent
import kotlinx.coroutines.flow.Flow

class LocationWebSocketDataSource(
    private val webSocketClient: WebSocketClient
) {
    fun connect() = webSocketClient.connect()
    fun disconnect() = webSocketClient.disconnect()
    fun events(): Flow<LocationEvent> = webSocketClient.events
    fun send(location: Location) = webSocketClient.send(location)
}
