package com.lossabinos.domain.usecases.WorkRequestPhoto

import com.lossabinos.domain.entities.WorkRequestPhoto
import com.lossabinos.domain.repositories.WorkRequestPhotoRepository

class SaveWorkRequestPhotoUseCase(
    private val repository: WorkRequestPhotoRepository
) {
    suspend operator fun invoke(photo: WorkRequestPhoto) {
        repository.savePhoto(photo)
    }
}
