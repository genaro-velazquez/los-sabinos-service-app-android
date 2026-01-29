package com.lossabinos.domain.valueobjects

import com.lossabinos.domain.entities.DomainEntity
import com.lossabinos.domain.enums.SyncStatus
import com.lossabinos.domain.enums.UrgencyLevel

class WorkRequest(
    val id: String,
    val workOrderId:String,
    val title:String,
    val description: String,
    val findings: String,
    val justification:String,
    val photoUls: List<String>,
    val requestType: String,
    val requiresCustomerApproval: Boolean,
    val urgency: UrgencyLevel,
    val createdAt: Long,
    val vehicleId:String,
    val syncStatus: SyncStatus
): DomainEntity()