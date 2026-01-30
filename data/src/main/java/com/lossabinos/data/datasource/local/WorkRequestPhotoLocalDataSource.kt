package com.lossabinos.data.datasource.local

import com.lossabinos.data.datasource.local.database.dao.WorkRequestPhotoDao
import com.lossabinos.data.datasource.local.database.entities.SyncStatusEntity
import com.lossabinos.data.datasource.local.database.entities.WorkRequestPhotoEntity
import com.lossabinos.data.mappers.toDomain
import com.lossabinos.data.mappers.toEntity
import com.lossabinos.domain.entities.WorkRequestPhoto
import javax.inject.Inject

class WorkRequestPhotoLocalDataSource @Inject constructor(
    private val workRequestPhotoDao: WorkRequestPhotoDao
) {

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
        photoId: String,
        remoteUrl: String
    ) {
        workRequestPhotoDao.markAsSynced(
            photoId = photoId,
            remoteUrl = remoteUrl,
            status = SyncStatusEntity.SYNCED
        )
    }


}