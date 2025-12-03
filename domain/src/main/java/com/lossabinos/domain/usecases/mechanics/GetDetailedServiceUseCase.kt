package com.lossabinos.domain.usecases.mechanics

import com.lossabinos.domain.repositories.MechanicsRepository

class GetDetailedServiceUseCase(
    private val mechanicsRepository: MechanicsRepository
) {
    suspend fun execute(idService:String) = mechanicsRepository.detailedService(idService = idService)
}