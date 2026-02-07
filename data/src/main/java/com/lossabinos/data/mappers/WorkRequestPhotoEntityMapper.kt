package com.lossabinos.data.mappers

import com.lossabinos.data.datasource.local.database.entities.SyncStatusEntity
import com.lossabinos.data.datasource.local.database.entities.WorkRequestPhotoEntity
import com.lossabinos.domain.entities.WorkRequestPhoto
import com.lossabinos.domain.enums.SyncStatus
import javax.inject.Inject

class WorkRequestPhotoEntityMapper @Inject constructor(

) {
    fun toEntity(domain: WorkRequestPhoto): WorkRequestPhotoEntity {
        return WorkRequestPhotoEntity(
            id = domain.id,
            workRequestId = domain.workRequestId,
            localPath = domain.localPath,
            remoteUrl = domain.remoteUrl,
            createdAt = domain.createdAt,
            syncStatus = SyncStatusEntity.valueOf(domain.syncStatus.name)
        )
    }

    fun toDomain(entity: WorkRequestPhotoEntity): WorkRequestPhoto {
        return WorkRequestPhoto(
            id = entity.id,
            workRequestId = entity.workRequestId,
            localPath = entity.localPath,
            remoteUrl = entity.remoteUrl,
            createdAt = entity.createdAt,
            syncStatus = SyncStatus.valueOf(entity.syncStatus.name)
        )
    }
}