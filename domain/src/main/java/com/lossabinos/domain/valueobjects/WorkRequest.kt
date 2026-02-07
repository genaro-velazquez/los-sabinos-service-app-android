package com.lossabinos.domain.valueobjects

import com.lossabinos.domain.entities.DomainEntity
import com.lossabinos.domain.enums.IssueCategoryType
import com.lossabinos.domain.enums.ConceptCategoryType
import com.lossabinos.domain.enums.SyncStatus
import com.lossabinos.domain.enums.UrgencyLevelType

data class WorkRequest(
    val id: String,
    val workOrderId:String,
    val serviceExecutionId: String,
    val title:String,
    val description: String,
    val findings: String,
    val justification:String,
    val photoUls: List<String>,
    val requestType: String,
    val requiresCustomerApproval: Boolean,
    val urgency: UrgencyLevelType,
    val issueCategory: IssueCategoryType,
    val conceptCategory: ConceptCategoryType,
    val createdAt: Long,
    val vehicleId:String,
    val syncStatus: SyncStatus
): DomainEntity()