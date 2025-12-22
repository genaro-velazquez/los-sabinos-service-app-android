package com.lossabinos.domain.usecases.checklist

import com.lossabinos.domain.repositories.ChecklistRepository

class GetTotalCompletedActivitiesUseCase(
    private val checklistRepository: ChecklistRepository
) {

    suspend operator fun invoke (
        assignedServiceId: String
    ) : Int{
        return checklistRepository.getTotalCompletedActivities(
            assignedServiceId = assignedServiceId
        )
    }

}