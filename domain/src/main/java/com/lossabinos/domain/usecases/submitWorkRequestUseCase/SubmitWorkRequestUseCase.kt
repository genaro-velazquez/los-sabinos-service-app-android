package com.lossabinos.domain.usecases.submitWorkRequestUseCase

import com.lossabinos.domain.entities.WorkRequestPhoto
import com.lossabinos.domain.enums.IssueCategoryType
import com.lossabinos.domain.enums.SeverityLevelType
import com.lossabinos.domain.enums.SyncStatus
import com.lossabinos.domain.enums.UrgencyLevelType
import com.lossabinos.domain.repositories.WorkRequestPhotoRepository
import com.lossabinos.domain.repositories.WorkRequestRepository
import com.lossabinos.domain.repositories.WorkRequestSyncScheduler
import com.lossabinos.domain.valueobjects.WorkRequest
import com.lossabinos.domain.valueobjects.WorkRequestIssue
import com.lossabinos.domain.valueobjects.withPhotoUrls

class SubmitWorkRequestUseCase(
    private val workRequestLocalRepository: WorkRequestRepository,
    private val workRequestPhotoLocalRepository: WorkRequestPhotoRepository,
    private val syncScheduler: WorkRequestSyncScheduler
){
    suspend operator fun invoke(
        workRequest: WorkRequest,
        photos: List<WorkRequestPhoto>
    ) {
        // 1️⃣ Guardar WorkRequest como PENDING
        workRequestLocalRepository.register(workRequest = workRequest)

        // 2️⃣ Guardar fotos como PENDING
        workRequestPhotoLocalRepository.registerAll(photos = photos)

        // 3️⃣ Disparar sincronización
        syncScheduler.enqueue()
    }
}

/*
class SubmitWorkRequestUseCase(
    private val workRequestRepository: WorkRequestRepository,
    private val workRequestPhotoRepository: WorkRequestPhotoRepository
) {

    suspend operator fun invoke(
        workRequest: WorkRequest,
        serviceExecutionId: String
    ) {

        // ✅ 1️⃣ CREAR ISSUE (sin fotos)
        workRequestRepository.createWorkRequestIssue(
            serviceExecutionId = serviceExecutionId,
            issue = WorkRequestIssue(
                description = workRequest.description,
                severity = UrgencyLevelType.valueOf(workRequest.urgency.name),
                category = IssueCategoryType.valueOf(workRequest.issueCategory.name)
            )
        )

        // ✅ 2️⃣ OBTENER FOTOS LOCALES
        val photos =
            workRequestPhotoRepository.getPhotos(workRequest.id)

        // 3️⃣ SUBIR FOTOS SOLO SI EXISTEN
        val uploadedPhotos = if (photos.isNotEmpty()) {
            workRequestPhotoRepository.uploadPhotos(
                serviceExecutionId = serviceExecutionId,
                photos = photos
            )
        } else {
            emptyList()
        }
print("---> photoUrls:${uploadedPhotos}")
print("---> photoUrls url:${uploadedPhotos[0].url}")
print("---> photoUrls description:${uploadedPhotos[0].description}")
print("---> photoUrls id:${uploadedPhotos[0].id}")
print("---> photoUrls uploadedAt:${uploadedPhotos[0].uploadedAt}")

        // ✅ 4️⃣ CREAR REPORTE PRIMERO (SIN FOTOS)
        val photoUrls: List<String> = uploadedPhotos.map { it.url }
print("---> photoUrls:$photoUrls")
        workRequestRepository.createWorkRequest(
            workRequest.withPhotoUrls(photoUrls)
        )
print("---> photoUrls:$photoUrls")
        // ✅ 5️⃣ MARCAR COMO SINCRONIZADAS
        workRequestPhotoRepository.markPhotosAsSynced(
            photos = photos,
            uploadedPhotos = uploadedPhotos
        )
    }
}
*/