package com.lossabinos.domain.usecases.checklist

import com.lossabinos.domain.entities.ObservationAnswer
import com.lossabinos.domain.repositories.ChecklistRepository

class GetObservationResponsesForSectionUseCase(
    private val checklistRepository: ChecklistRepository
) {

    suspend operator fun invoke(
        assignedServiceId: String,
        sectionIndex: Int
    ): List<ObservationAnswer>{
        return checklistRepository.getObservationResponsesForSection(
            assignedServiceId = assignedServiceId,
            sectionIndex = sectionIndex
        )
    }
}