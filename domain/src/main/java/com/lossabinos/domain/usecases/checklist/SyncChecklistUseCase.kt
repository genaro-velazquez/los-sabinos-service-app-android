package com.lossabinos.domain.usecases.checklist

import com.lossabinos.domain.repositories.ChecklistRepository
import com.lossabinos.domain.responses.SyncResult

class SyncChecklistUseCase(
    private val checklistRepository: ChecklistRepository
) {
    suspend operator fun invoke(serviceId: String) : SyncResult {
        // 1️⃣ Validar
        val isSynced = checklistRepository.isServiceSynced(serviceId)

        if (isSynced) {
            return SyncResult.AlreadySynced
        }

        return try {
            // 2️⃣ Ejecutar sincronización
            checklistRepository.syncChecklist(serviceId)
            SyncResult.Success
        } catch (e: Exception) {
            SyncResult.Error(e.message ?: "Error desconocido")
        }

        //checklistRepository.syncChecklist(serviceId)
    }
}