package com.lossabinos.domain.usecases.WorkRequestPhoto

import com.lossabinos.domain.entities.WorkRequestPhoto
import com.lossabinos.domain.repositories.WorkRequestPhotoRepository

class GetWorkRequestPhotosUseCase(
    private val repository: WorkRequestPhotoRepository
) {
    suspend operator fun invoke(
        workRequestId: String
    ): List<WorkRequestPhoto> {
        return repository.getPhotos(workRequestId)
    }
}
