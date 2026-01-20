package com.lossabinos.domain.usecases

import com.lossabinos.domain.repositories.LocationSocketRepository

class ConnectLocationSocketUseCase(
    private val repository: LocationSocketRepository
) {
    operator fun invoke() = repository.connect()
}
