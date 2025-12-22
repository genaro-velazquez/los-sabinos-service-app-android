package com.lossabinos.domain.usecases.checklist

import com.lossabinos.domain.entities.ActivityEvidence
import com.lossabinos.domain.repositories.ChecklistRepository

class GetEvidenceForActivityUseCase(
    private val checklistRepository: ChecklistRepository
) {

    suspend operator fun invoke(
        activityProgressId: Long
    ) : List<ActivityEvidence>{
        return checklistRepository.getEvidenceForActivity(
            activityProgressId = activityProgressId
        )
    }

}