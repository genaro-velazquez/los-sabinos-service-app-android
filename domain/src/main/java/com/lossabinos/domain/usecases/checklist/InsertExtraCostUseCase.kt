package com.lossabinos.domain.usecases.checklist

import com.lossabinos.domain.entities.ExtraCost
import com.lossabinos.domain.repositories.ChecklistRepository

class InsertExtraCostUseCase(
    private val repository: ChecklistRepository
) {
    suspend operator fun invoke(
        extraCost: ExtraCost
    ){
        repository.insertExtraCost(extraCost = extraCost)
    }
}