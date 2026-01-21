package com.lossabinos.domain.usecases.checklist

import com.lossabinos.domain.responses.SyncResult

class SyncAndSignChecklistUseCase(
    private val syncChecklistUseCase: SyncChecklistUseCase,
    private val signChecklistUseCase: SignChecklistUseCase
) {

    suspend operator fun invoke(serviceId: String): SyncResult {

        // 1️⃣ Sincronizar
        when (val syncResult = syncChecklistUseCase(serviceId)) {
            SyncResult.Success -> Unit
            else -> return syncResult
        }

        // 2️⃣ Firmar
        return try {
            val signResponse = signChecklistUseCase(serviceId)

            if (signResponse.success) {
                SyncResult.Success
            } else {
                SyncResult.Error(signResponse.messages)
            }

        } catch (e: Exception) {
            SyncResult.Error("Checklist sincronizado pero no firmado")
        }
    }
}
