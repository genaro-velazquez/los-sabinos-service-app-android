package com.lossabinos.domain.repositories

import com.lossabinos.domain.entities.WorkRequestPhoto


interface WorkRequestPhotoRepository {
    suspend fun savePhoto(photo: WorkRequestPhoto)
    suspend fun getPhotos(workRequestId: String): List<WorkRequestPhoto>
    suspend fun deletePhoto(photoId: String)
}
