package com.lossabinos.domain.usecases.checklist

import com.lossabinos.domain.entities.ActivityEvidence
import com.lossabinos.domain.repositories.ChecklistRepository

class SaveActivityEvidenceUseCase(
    private val checklistRepository: ChecklistRepository
) {

    suspend operator fun invoke(
        id: Long,
        activityProgressId: Long,
        filePath: String,
        fileType: String = "image"
    ) : Long {
        return checklistRepository.saveActivityEvidence(
            id = id,
            activityProgressId = activityProgressId,
            filePath = filePath,
            fileType = fileType
        )
    }

}