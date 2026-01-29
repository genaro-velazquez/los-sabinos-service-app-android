package com.lossabinos.data.mappers

import com.lossabinos.data.datasource.local.database.entities.SyncStatusEntity
import com.lossabinos.data.datasource.local.database.entities.WorkRequestPhotoEntity
import com.lossabinos.domain.entities.WorkRequestPhoto
import com.lossabinos.domain.enums.SyncStatus

fun WorkRequestPhoto.toEntity(): WorkRequestPhotoEntity {
    return WorkRequestPhotoEntity(
        id = id,
        workRequestId = workRequestId,
        localPath = localPath,
        remoteUrl = remoteUrl,
        syncStatus = SyncStatusEntity.valueOf(syncStatus.name),
        createdAt = createdAt
    )
}

fun WorkRequestPhotoEntity.toDomain(): WorkRequestPhoto {
    return WorkRequestPhoto(
        id = id,
        workRequestId = workRequestId,
        localPath = localPath,
        remoteUrl = remoteUrl,
        syncStatus = SyncStatus.valueOf(syncStatus.name),
        createdAt = createdAt
    )
}
