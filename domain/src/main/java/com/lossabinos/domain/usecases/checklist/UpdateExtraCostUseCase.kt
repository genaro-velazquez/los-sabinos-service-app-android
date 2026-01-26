package com.lossabinos.domain.usecases.checklist

import com.lossabinos.domain.entities.ExtraCost
import com.lossabinos.domain.repositories.ChecklistRepository

class UpdateExtraCostUseCase(
    private val repository: ChecklistRepository
) {
    suspend operator fun invoke(
        extraCost: ExtraCost
    ){
        repository.updateExtraCost(
            extraCost = extraCost
        )
    }
}