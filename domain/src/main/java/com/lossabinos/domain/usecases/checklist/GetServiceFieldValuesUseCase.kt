package com.lossabinos.domain.usecases.checklist

import com.lossabinos.domain.entities.ServiceFieldValue
import com.lossabinos.domain.repositories.ChecklistRepository

class GetServiceFieldValuesUseCase(
    private val checklistRepository: ChecklistRepository
) {
    suspend operator fun invoke(
        assignedServiceId: String
    ) : List<ServiceFieldValue>{
        return checklistRepository.getServiceFieldValues(
            assignedServiceId = assignedServiceId
        )
    }
}