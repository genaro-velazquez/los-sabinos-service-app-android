package com.lossabinos.domain.usecases.checklist

import com.lossabinos.domain.repositories.ChecklistRepository

class CompleteActivityUseCase(
    private val checklistRepository: ChecklistRepository
) {
    suspend operator fun invoke(
        assignedServiceId: String,
        sectionIndex: Int,
        activityIndex: Int,
        description: String,
        requiresEvidence: Boolean,
        completed: Boolean = true,
        completedAt: String = System.currentTimeMillis().toString()
    ) : Long{
        return checklistRepository.saveActivityProgress(
            assignedServiceId = assignedServiceId,
            sectionIndex = sectionIndex,
            activityIndex = activityIndex,
            description = description,
            requiresEvidence = requiresEvidence,
            completed = completed,
            completedAt = completedAt
        )
    }
}