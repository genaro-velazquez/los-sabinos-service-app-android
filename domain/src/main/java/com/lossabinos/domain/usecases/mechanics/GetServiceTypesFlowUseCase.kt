package com.lossabinos.domain.usecases.mechanics

import com.lossabinos.domain.entities.ServiceType
import com.lossabinos.domain.repositories.MechanicsRepository
import kotlinx.coroutines.flow.Flow

class GetServiceTypesFlowUseCase(
    private val repository: MechanicsRepository
) {
    operator fun invoke(): Flow<List<ServiceType>> {
        return repository.getServiceTypesFlow()
    }
}
