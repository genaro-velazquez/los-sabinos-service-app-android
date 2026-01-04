package com.lossabinos.domain.usecases.mechanics

import com.lossabinos.domain.repositories.MechanicsRepository

class UpdateServiceProgressUseCase(
    private val repository: MechanicsRepository
) {
    suspend operator fun invoke(
        serviceId: String,
        completedActivities: Int,
        totalActivities: Int
    ) {
        repository.updateServiceProgress(
            serviceId = serviceId,
            completedActivities = completedActivities,
            totalActivities = totalActivities
        )
    }
}