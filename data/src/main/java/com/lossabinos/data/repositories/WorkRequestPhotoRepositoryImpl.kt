package com.lossabinos.data.repositories

import com.lossabinos.data.datasource.local.WorkRequestPhotoLocalDataSource
import com.lossabinos.data.datasource.local.database.dao.WorkRequestPhotoDao
import com.lossabinos.data.mappers.toDomain
import com.lossabinos.data.mappers.toEntity
import com.lossabinos.domain.entities.WorkRequestPhoto
import com.lossabinos.domain.repositories.WorkRequestPhotoRepository

class WorkRequestPhotoRepositoryImpl(
    private val workRequestPhotoLocalDataSource: WorkRequestPhotoLocalDataSource
) : WorkRequestPhotoRepository {

    override suspend fun savePhoto(photo: WorkRequestPhoto) {
        workRequestPhotoLocalDataSource.savePhoto(photo = photo.toEntity())
    }

    override suspend fun getPhotos(
        workRequestId: String
    ): List<WorkRequestPhoto> {
        return workRequestPhotoLocalDataSource
            .getPhotos(workRequestId).map { it.toDomain() }
    }

    override suspend fun deletePhoto(photoId: String) {
        workRequestPhotoLocalDataSource.deletePhoto(photoId)
    }
}
