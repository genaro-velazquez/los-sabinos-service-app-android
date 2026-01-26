package com.lossabinos.domain.usecases.checklist

import com.lossabinos.domain.entities.ExtraCost
import com.lossabinos.domain.repositories.ChecklistRepository
import kotlinx.coroutines.flow.Flow

class ObserveExtraCostsUseCase(
    private val repository: ChecklistRepository
) {
    operator fun invoke(
        assignedServiceId: String
    ): Flow<List<ExtraCost>>{
        return repository.observeExtraCosts(
            assignedServiceId = assignedServiceId
        )
    }
}