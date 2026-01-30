package com.lossabinos.domain.repositories

import com.lossabinos.domain.entities.Photo
import com.lossabinos.domain.entities.WorkRequestPhoto
import com.lossabinos.domain.responses.UploadPhotoResponse


interface WorkRequestPhotoRepository {
    suspend fun savePhoto(photo: WorkRequestPhoto)
    suspend fun getPhotos(workRequestId: String): List<WorkRequestPhoto>
    suspend fun deletePhoto(photoId: String)
    suspend fun uploadPhotos(
        serviceExecutionId: String,
        photos: List<WorkRequestPhoto>
    ): List<Photo>
    suspend fun markAsSynced(
        photoId: String,
        remoteUrl: String
    )

    suspend fun markPhotosAsSynced(
        photos: List<WorkRequestPhoto>,
        uploadedPhotos: List<Photo>
    )

}
