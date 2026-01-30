package com.lossabinos.domain.usecases.submitWorkRequestUseCase

import com.lossabinos.domain.repositories.WorkRequestPhotoRepository
import com.lossabinos.domain.repositories.WorkRequestRepository
import com.lossabinos.domain.valueobjects.WorkRequest
import com.lossabinos.domain.valueobjects.withPhotoUrls

class SubmitWorkRequestUseCase(
    private val workRequestRepository: WorkRequestRepository,
    private val workRequestPhotoRepository: WorkRequestPhotoRepository
) {

    suspend operator fun invoke(
        workRequest: WorkRequest,
        serviceExecutionId: String
    ) {

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
