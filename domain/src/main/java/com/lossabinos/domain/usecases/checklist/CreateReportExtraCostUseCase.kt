package com.lossabinos.domain.usecases.checklist

import com.lossabinos.domain.repositories.ChecklistRepository

class CreateReportExtraCostUseCase(
    private val repository: ChecklistRepository
) {
    suspend operator fun invoke(
        idExecutionService:Int
    ){
        repository.createReportExtraCost(
            idExecutionService = idExecutionService
        )
    }
}