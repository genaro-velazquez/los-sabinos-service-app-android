package com.lossabinos.domain.usecases.submitWorkRequestUseCase

import com.lossabinos.domain.enums.IssueCategoryType
import com.lossabinos.domain.enums.SeverityLevelType
import com.lossabinos.domain.enums.UrgencyLevelType
import com.lossabinos.domain.repositories.WorkRequestPhotoRepository
import com.lossabinos.domain.repositories.WorkRequestRepository
import com.lossabinos.domain.valueobjects.WorkRequest
import com.lossabinos.domain.valueobjects.WorkRequestIssue
import com.lossabinos.domain.valueobjects.withPhotoUrls

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

/*
        // ✅ 1️⃣ CREAR REPORTE PRIMERO (SIN FOTOS)
        workRequestRepository.createWorkRequest(
            workRequest.withPhotoUrls(emptyList())
        )

        // ✅ 2️⃣ OBTENER FOTOS LOCALES
        val photos =
            workRequestPhotoRepository.getPhotos(workRequest.id)

        if (photos.isEmpty()) return

        // ✅ 3️⃣ SUBIR FOTOS (YA EXISTE EL REPORTE)
        val uploadedPhotos =
            workRequestPhotoRepository.uploadPhotos(
                serviceExecutionId,
                photos
            )

        // ✅ 4️⃣ MARCAR COMO SINCRONIZADAS
        workRequestPhotoRepository.markPhotosAsSynced(
            photos,
            uploadedPhotos
        )
*/

/*
        // 1️⃣ Obtener fotos locales
        val photos =
            workRequestPhotoRepository.getPhotos(workRequest.id)

        // 2️⃣ Subir TODAS las fotos (si una falla → exception)
        val uploadedPhotos =
            if (photos.isNotEmpty()) {
                workRequestPhotoRepository.uploadPhotos(
                    serviceExecutionId,
                    photos
                )
            } else {
                emptyList()
            }

        // 3️⃣ Crear request con URLs
        val requestWithPhotos =
            workRequest.withPhotoUrls(
                uploadedPhotos.map { it.url }
            )

        // 4️⃣ Crear reporte en backend
        workRequestRepository.createWorkRequest(requestWithPhotos)

        // 2️⃣ commit final
        workRequestPhotoRepository.markPhotosAsSynced(
            photos,
            uploadedPhotos
        )
*/
    }
}
