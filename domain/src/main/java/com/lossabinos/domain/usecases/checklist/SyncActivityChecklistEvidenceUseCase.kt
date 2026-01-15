package com.lossabinos.domain.usecases.checklist

import com.lossabinos.domain.repositories.ChecklistRepository

class SyncActivityChecklistEvidenceUseCase(
    private val checklistRepository: ChecklistRepository
) {
    suspend operator fun invoke(
        serviceId: String,
        activityId: String
    ) {
        checklistRepository.syncActivityChecklistEvidence(
            serviceId = serviceId,
            activityId = activityId
        )
    }
}