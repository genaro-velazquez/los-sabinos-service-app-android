package com.lossabinos.domain.usecases.WorkRequestPhoto

import com.lossabinos.domain.entities.Photo
import com.lossabinos.domain.entities.WorkRequestPhoto
import com.lossabinos.domain.repositories.WorkRequestPhotoRepository
import com.lossabinos.domain.responses.UploadPhotoResponse

class UploadPhotoUseCase(
    private val repository: WorkRequestPhotoRepository
) {

    suspend operator fun invoke(
        serviceExecutionId: String,
        photos: List<WorkRequestPhoto>
    ): List<Photo> {
        return repository.uploadPhotos(
            serviceExecutionId = serviceExecutionId,
            photos = photos
        )
    }
}