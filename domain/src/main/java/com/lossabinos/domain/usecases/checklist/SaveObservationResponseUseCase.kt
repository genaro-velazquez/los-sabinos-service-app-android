package com.lossabinos.domain.usecases.checklist

import com.lossabinos.domain.repositories.ChecklistRepository

class SaveObservationResponseUseCase(
    private val checklistRepository: ChecklistRepository
) {
    suspend operator fun invoke(
        assignedServiceId: String,
        sectionIndex: Int,
        observationIndex: Int,
        observationId: String,
        observationDescription: String,
        responseType: String,
        response: String? = null,
        requiresResponse: Boolean = false
    ) : Long {
        return checklistRepository.saveObservationResponse(
            assignedServiceId = assignedServiceId,
            sectionIndex = sectionIndex,
            observationIndex = observationIndex,
            observationId = observationId,
            observationDescription = observationDescription,
            responseType = responseType,
            response = response,
            requiresResponse = requiresResponse
        )
    }
}