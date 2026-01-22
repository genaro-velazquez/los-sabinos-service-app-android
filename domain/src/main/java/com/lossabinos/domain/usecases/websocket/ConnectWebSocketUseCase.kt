package com.lossabinos.domain.usecases.websocket

import com.lossabinos.domain.repositories.WebSocketRepository

class ConnectWebSocketUseCase(
    private val webSocketRepository: WebSocketRepository
) {
    operator fun invoke(accessToken: String) {
        webSocketRepository.connect(accessToken)
    }
}
