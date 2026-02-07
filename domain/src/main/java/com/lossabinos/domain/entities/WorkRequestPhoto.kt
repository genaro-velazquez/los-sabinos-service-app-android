package com.lossabinos.domain.entities

import com.lossabinos.domain.enums.SyncStatus

data class WorkRequestPhoto(
    val id: String,
    val workRequestId: String,
    val localPath: String,
    val remoteUrl: String?,
    val syncStatus: SyncStatus,
    val createdAt: Long
): DomainEntity()
