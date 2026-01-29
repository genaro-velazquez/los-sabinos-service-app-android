package com.lossabinos.domain.usecases.WorkRequestPhoto

import com.lossabinos.domain.repositories.WorkRequestPhotoRepository

class DeleteWorkRequestPhotoUseCase(
    private val repository: WorkRequestPhotoRepository
) {
    suspend operator fun invoke(photoId: String) {
        repository.deletePhoto(photoId)
    }
}
