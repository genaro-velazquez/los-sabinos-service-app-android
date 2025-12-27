package com.lossabinos.domain.usecases.checklist

import com.lossabinos.domain.repositories.ChecklistRepository

class DeleteActivityEvidenceByIdUseCase(
    private val checklistRepository: ChecklistRepository
) {
    suspend operator fun invoke(evidenceId: Long){
        checklistRepository.deleteActivityEvidenceById(evidenceId = evidenceId)
    }
}