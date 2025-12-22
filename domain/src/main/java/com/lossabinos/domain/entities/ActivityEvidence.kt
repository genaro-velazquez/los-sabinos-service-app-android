package com.lossabinos.domain.entities

class ActivityEvidence(
    val id: Long,
    val activityProgressId: Long,
    val filePath: String,
    val fileType: String = "image",
    val timestamp: String
): DomainEntity()