package com.lossabinos.domain.usecases.mechanics

import com.lossabinos.domain.repositories.MechanicsRepository

class GetMechanicsServicesUseCase(
    private val mechanicsRepository: MechanicsRepository
) {
    suspend fun execute() = mechanicsRepository.assignedServices()
}