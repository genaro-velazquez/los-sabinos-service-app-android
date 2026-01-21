package com.lossabinos.domain.usecases.checklist

import com.lossabinos.domain.repositories.ChecklistRepository
import com.lossabinos.domain.responses.SignChecklistResponse

class SignChecklistUseCase(
    private val checklistRepository: ChecklistRepository
) {
    suspend operator fun invoke(
        serviceExecutionId:String
    ) : SignChecklistResponse {
        return checklistRepository.signChecklist(
            serviceExecutionId = serviceExecutionId
        )
    }
}