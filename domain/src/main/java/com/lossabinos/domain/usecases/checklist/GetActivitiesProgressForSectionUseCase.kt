package com.lossabinos.domain.usecases.checklist

import com.lossabinos.domain.entities.ActivityProgress
import com.lossabinos.domain.repositories.ChecklistRepository

class GetActivitiesProgressForSectionUseCase(
    private val checklistRepository: ChecklistRepository
) {

    suspend operator fun invoke(
        assignedServiceId: String,
        sectionIndex: Int
    ) : List<ActivityProgress>{
        return checklistRepository.getActivitiesProgressForSection(
            assignedServiceId = assignedServiceId,
            sectionIndex = sectionIndex
        )
    }

}