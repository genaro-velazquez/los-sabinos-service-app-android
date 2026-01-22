package com.lossabinos.domain.usecases.websocket

import com.lossabinos.domain.repositories.WebSocketRepository
import kotlinx.coroutines.flow.Flow

class ObserveWebSocketMessagesUseCase (
    private val webSocketRepository: WebSocketRepository
) {
    operator fun invoke(): Flow<String> = webSocketRepository.observeMessages()
}
