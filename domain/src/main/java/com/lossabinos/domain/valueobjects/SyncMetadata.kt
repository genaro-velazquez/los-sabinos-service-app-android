package com.lossabinos.domain.valueobjects

import com.lossabinos.domain.entities.DomainEntity

class SyncMetadata(
    val serverTimestamp:String,
    val totalServices: Int,
    val pendingServices:Int,
    val inProgressServices:Int,
    val lastSync: String
) : DomainEntity()