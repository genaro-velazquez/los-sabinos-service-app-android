package com.lossabinos.domain.usecases

import com.lossabinos.domain.models.LocationEvent
import com.lossabinos.domain.repositories.LocationSocketRepository
import kotlinx.coroutines.flow.Flow

class ObserveLocationUseCase(
    private val repository: LocationSocketRepository
) {
    operator fun invoke(): Flow<LocationEvent> =
        repository.observeLocation()
}
