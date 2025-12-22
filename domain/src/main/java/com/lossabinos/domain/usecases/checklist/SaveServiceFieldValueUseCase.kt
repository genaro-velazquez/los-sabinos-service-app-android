package com.lossabinos.domain.usecases.checklist

import com.lossabinos.domain.repositories.ChecklistRepository

class SaveServiceFieldValueUseCase(
    private val checklistRepository: ChecklistRepository
)  {
    suspend operator fun invoke(
        assignedServiceId: String,
        fieldIndex: Int,
        fieldLabel: String,
        fieldType: String,
        required: Boolean,
        value: String?
    ): Long{
        return checklistRepository.saveServiceFieldValue(
            assignedServiceId = assignedServiceId,
            fieldIndex = fieldIndex,
            fieldLabel = fieldLabel,
            fieldType = fieldType,
            required = required,
            value = value
        )
    }
}