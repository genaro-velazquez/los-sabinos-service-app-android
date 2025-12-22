package com.lossabinos.domain.usecases.checklist

import com.lossabinos.domain.repositories.ChecklistRepository

class SaveObservationResponseUseCase(
    private val checklistRepository: ChecklistRepository
) {
    suspend operator fun invoke(
        assignedServiceId: String,
        sectionIndex: Int,
        observationIndex: Int,
        observationDescription: String,
        response: String
    ) : Long {
        return checklistRepository.saveObservationResponse(
            assignedServiceId = assignedServiceId,
            sectionIndex = sectionIndex,
            observationIndex = observationIndex,
            observationDescription = observationDescription,
            response = response
        )
    }
}