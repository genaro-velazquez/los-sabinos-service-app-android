package com.lossabinos.domain.usecases

import com.lossabinos.domain.repositories.LocationSocketRepository

class DisconnectLocationSocketUseCase(
    private val repository: LocationSocketRepository
) {
    operator fun invoke() = repository.disconnect()
}
