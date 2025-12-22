package com.lossabinos.domain.usecases.checklist

import com.lossabinos.domain.entities.ServiceFieldValue
import com.lossabinos.domain.repositories.ChecklistRepository

class SaveServiceFieldValuesUseCase(
    private val checklistRepository: ChecklistRepository
) {

    suspend operator fun invoke(
        assignedServiceId: String,
        fields: List<ServiceFieldValue>
    ){
        checklistRepository.saveServiceFieldValues(
            assignedServiceId = assignedServiceId,
            fields = fields
        )
    }

}