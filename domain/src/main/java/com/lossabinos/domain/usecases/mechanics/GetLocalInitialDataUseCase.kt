package com.lossabinos.domain.usecases.mechanics

import com.lossabinos.domain.repositories.MechanicsRepository
import com.lossabinos.domain.responses.InitialDataResponse

class GetLocalInitialDataUseCase(
    private val mechanicsRepository: MechanicsRepository
) {
    suspend operator fun invoke(): InitialDataResponse? {
        return try {
            mechanicsRepository.getLocalInitialData()
        } catch (e: Exception) {
            println("❌ Error cargando datos locales: ${e.message}")
            null
        }
    }

}