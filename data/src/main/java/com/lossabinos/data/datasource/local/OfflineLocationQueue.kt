package com.lossabinos.data.datasource.local

import com.lossabinos.data.datasource.local.database.AppDatabase
import com.lossabinos.data.datasource.local.database.entities.OfflineLocationEntity
import com.lossabinos.domain.models.Location
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class OfflineLocationQueue @Inject constructor(
    private val database: AppDatabase,
    private val webSocketClient: WebSocketClient
) {
    suspend fun enqueue(location: Location) {
        val json = Json.encodeToString(location)
        val entity = OfflineLocationEntity(jsonMessage = json)
        database.offlineLocationDao().insert(entity)
    }

    suspend fun processQueue() {
        val pending = database.offlineLocationDao().getAll()
        pending.forEach { entity ->
            // Decode the JSON string back into a Location object
            val location: Location = Json.decodeFromString(entity.jsonMessage)
            // Send it through the WebSocket
            webSocketClient.send(location)
            // Delete the processed entry from the queue
            database.offlineLocationDao().delete(entity)
        }
    }
}
