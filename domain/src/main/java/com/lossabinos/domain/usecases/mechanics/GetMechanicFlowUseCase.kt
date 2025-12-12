package com.lossabinos.domain.usecases.mechanics

import com.lossabinos.domain.entities.Mechanic
import com.lossabinos.domain.repositories.MechanicsRepository
import kotlinx.coroutines.flow.Flow

class GetMechanicFlowUseCase(
    private val repository: MechanicsRepository
) {
    operator fun invoke(): Flow<Mechanic?> {
        return repository.getMechanicFlow()
    }
}