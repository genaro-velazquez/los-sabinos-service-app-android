package com.lossabinos.domain.usecases.submitWorkRequestUseCase

import com.lossabinos.domain.repositories.IssueRepository
import com.lossabinos.domain.repositories.WorkRequestPhotoRepository
import com.lossabinos.domain.repositories.WorkRequestRepository
import com.lossabinos.domain.valueobjects.WorkRequest
import com.lossabinos.domain.valueobjects.WorkRequestIssue

class SyncWorkRequestUseCase(
    private val workRequestRepository: WorkRequestRepository,
    private val workRequestPhotoRepository: WorkRequestPhotoRepository,
    private val issueRepository: IssueRepository
) {

    suspend fun execute() {
        val pendingRequests =
            workRequestRepository.getPending()

        pendingRequests.forEach { request ->
            syncSingleRequest(request)
        }
    }

    private suspend fun syncSingleRequest(
        workRequest: WorkRequest,
    ) {
        // 1️⃣ Crear Issue
        val issueId = issueRepository.createIssue(
            serviceExecutionId = workRequest.serviceExecutionId,
            issue = WorkRequestIssue(
                description = workRequest.description,
                severity = workRequest.urgency,
                category = workRequest.issueCategory
            )
        )

        // 2️⃣ Obtener y subir fotos
        val pendingPhotos =
            workRequestPhotoRepository.getPendingByWorkRequest(workRequest.id)

        val uploadedPhotos =
            if (pendingPhotos.isNotEmpty()) {
                workRequestPhotoRepository.uploadPhotos(
                    serviceExecutionId = workRequest.serviceExecutionId,
                    photos = pendingPhotos
                )
            } else emptyList()

        if (uploadedPhotos.isNotEmpty()) {
            workRequestPhotoRepository.markAsSynced(uploadedPhotos)
        }

        // 3️⃣ 👇 CERRAR EL CICLO
        workRequestRepository.markAsSynced(workRequest.id)
    }
}