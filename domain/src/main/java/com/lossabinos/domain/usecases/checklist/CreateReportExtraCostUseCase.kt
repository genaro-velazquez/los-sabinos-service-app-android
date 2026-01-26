package com.lossabinos.domain.usecases.checklist

import com.lossabinos.domain.repositories.ChecklistRepository

class CreateReportExtraCostUseCase(
    private val repository: ChecklistRepository
) {
    suspend operator fun invoke(
        idExecutionService: String,
        amount: Double,
        category:String,
        description: String,
        notes: String
    ){
        repository.createReportExtraCost(
            idExecutionService = idExecutionService,
            amount = amount,
            category = category,
            description = description,
            notes = notes
        )
    }
}