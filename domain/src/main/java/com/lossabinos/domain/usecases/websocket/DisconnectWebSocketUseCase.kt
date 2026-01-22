package com.lossabinos.domain.usecases.websocket

import com.lossabinos.domain.repositories.WebSocketRepository

class DisconnectWebSocketUseCase (
    private val webSocketRepository: WebSocketRepository
) {
    operator fun invoke() {
        webSocketRepository.disconnect()
    }
}
