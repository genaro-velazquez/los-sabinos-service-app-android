package com.lossabinos.data.datasource.local.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "work_request_photo",
    foreignKeys = [
        ForeignKey(
            entity = WorkRequestEntity::class,
            parentColumns = ["id"],
            childColumns = ["workRequestId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("workRequestId")]
)
data class WorkRequestPhotoEntity(
    @PrimaryKey val id: String,
    val workRequestId: String,
    val localPath: String,
    val remoteUrl: String?,
    val syncStatus: SyncStatusEntity,
    val createdAt: Long
)
