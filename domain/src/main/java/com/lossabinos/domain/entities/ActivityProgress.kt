package com.lossabinos.domain.entities

class ActivityProgress(
    val id:Long,
    val assignedServiceId: String,
    val sectionIndex: Int,
    val activityIndex: Int,
    val activityId: String,
    val activityDescription: String,
    val requiresEvidence: Boolean,
    val completed: Boolean = false,
    val completedAt: String? = null
) : DomainEntity()