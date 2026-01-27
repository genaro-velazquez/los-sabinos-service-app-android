package com.lossabinos.domain.usecases.checklist

import com.lossabinos.domain.repositories.ChecklistRepository

class StartServiceUseCase(
    private val repository: ChecklistRepository
) {

    suspend operator fun invoke(
        serviceExecutionId: String
    ) {
        repository.startService(
            serviceExecutionId = serviceExecutionId
        )
    }

}