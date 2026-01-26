package com.lossabinos.domain.usecases.checklist

import com.lossabinos.domain.repositories.ChecklistRepository

class DeleteExtraCostUseCase(
    private val repository: ChecklistRepository
) {
    suspend operator fun invoke(
        id: String
    ){
        repository.deleteExtraCost(id = id)
    }
}