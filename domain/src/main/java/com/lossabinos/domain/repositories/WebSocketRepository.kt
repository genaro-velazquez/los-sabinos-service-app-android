package com.lossabinos.domain.repositories

import com.lossabinos.domain.valueobjects.LocationEvent
import com.lossabinos.domain.valueobjects.LocationVO
import kotlinx.coroutines.flow.Flow

interface WebSocketRepository {
    fun connect(accessToken: String)
    fun disconnect()
    fun sendMessage(message: String)
    fun isConnected(): Boolean
    fun observeMessages(): Flow<String>
    fun observeConnectionStatus(): Flow<Boolean>
}
