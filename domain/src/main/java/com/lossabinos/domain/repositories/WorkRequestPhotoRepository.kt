package com.lossabinos.domain.repositories

import com.lossabinos.domain.entities.Photo
import com.lossabinos.domain.entities.WorkRequestPhoto
import com.lossabinos.domain.responses.UploadPhotoResponse
import com.lossabinos.domain.valueobjects.UploadedPhoto


interface WorkRequestPhotoRepository {
    suspend fun savePhoto(photo: WorkRequestPhoto)
    suspend fun getPhotos(workRequestId: String): List<WorkRequestPhoto>
    suspend fun deletePhoto(photoId: String)
    suspend fun uploadPhotos(
        serviceExecutionId: String,
        photos: List<WorkRequestPhoto>
    ): List<UploadedPhoto>
    suspend fun markAsSynced(
        uploadedPhotos: List<UploadedPhoto>
    )
/*
    suspend fun markPhotosAsSynced(
        photos: List<WorkRequestPhoto>,
        uploadedPhotos: List<Photo>
    )
*/
    suspend fun registerAll(photos: List<WorkRequestPhoto>)

    suspend fun getPendingByWorkRequest(
        workRequestId: String
    ): List<WorkRequestPhoto>

    suspend fun deleteByWorkRequestId(workRequestId: String)

}
