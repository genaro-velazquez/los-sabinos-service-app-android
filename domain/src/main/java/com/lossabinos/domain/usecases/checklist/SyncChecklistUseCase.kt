package com.lossabinos.domain.usecases.checklist

import com.lossabinos.domain.repositories.ChecklistRepository

class SyncChecklistUseCase(
    private val checklistRepository: ChecklistRepository
) {
    suspend operator fun invoke(serviceId: String) {
        checklistRepository.syncChecklist(serviceId)
    }
}