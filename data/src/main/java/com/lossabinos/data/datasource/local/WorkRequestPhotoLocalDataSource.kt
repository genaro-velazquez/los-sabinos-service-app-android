package com.lossabinos.data.datasource.local

import com.lossabinos.data.datasource.local.database.dao.WorkRequestPhotoDao
import com.lossabinos.data.datasource.local.database.entities.SyncStatusEntity
import com.lossabinos.data.datasource.local.database.entities.WorkRequestPhotoEntity
import com.lossabinos.data.mappers.toDomain
import com.lossabinos.data.mappers.toEntity
import com.lossabinos.domain.entities.WorkRequestPhoto
import com.lossabinos.domain.valueobjects.UploadedPhoto
import javax.inject.Inject

class WorkRequestPhotoLocalDataSource @Inject constructor(
    private val workRequestPhotoDao: WorkRequestPhotoDao
) {

    suspend fun savePhotos(photos: List<WorkRequestPhotoEntity>){
        workRequestPhotoDao.insertAll(photos)
    }

    suspend fun getByWorkRequestAndStatus(
        workRequestId: String,
        status: SyncStatusEntity
    ): List<WorkRequestPhotoEntity> {
        return workRequestPhotoDao.getByWorkRequestAndStatus(
            workRequestId = workRequestId,
            status = status
        )
    }

    suspend fun savePhoto(photo: WorkRequestPhotoEntity){
        workRequestPhotoDao.insert(photo = photo)
    }

    suspend fun getPhotos(
        workRequestId: String
    ): List<WorkRequestPhotoEntity> {
        return workRequestPhotoDao.getPhotosForWorkRequest(workRequestId = workRequestId)
    }

    suspend fun deletePhoto(photoId: String) {
        workRequestPhotoDao.deleteById(photoId = photoId)
    }

    suspend fun markAsSynced(
        uploadedPhotos: List<UploadedPhoto>
    ) {
        uploadedPhotos.forEach { uploaded ->
            workRequestPhotoDao.updateAsSynced(
                photoId = uploaded.localPhotoId,
                remoteUrl = uploaded.remoteUrl
            )
        }
    }


}