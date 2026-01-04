package com.lossabinos.domain.usecases.mechanics

import com.lossabinos.domain.entities.AssignedService
import com.lossabinos.domain.repositories.MechanicsRepository
import com.lossabinos.domain.valueobjects.AssignedServiceProgress
import kotlinx.coroutines.flow.Flow

class GetAssignedServicesFlowUseCase(
    private val repository: MechanicsRepository
) {
    operator fun invoke(): Flow<List<AssignedServiceProgress>> {
        return repository.getAssignedServicesFlow()
    }
}