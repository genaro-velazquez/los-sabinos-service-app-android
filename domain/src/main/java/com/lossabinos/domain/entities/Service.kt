package com.lossabinos.domain.entities

class Service(
    val id: String,
    val executionId: String,
    val serviceType: ServiceType,
    val status: String,
    val priority: String,
    val scheduledStart: String?,
    val scheduledEnd: String?,
    val actualStart:String?,
    val actualEnd:String?,
    val progressPercentage: Int,
    val checklistTemplateId:String?,
    val hasTemplate: Boolean,
    val hasStarted: Boolean,
    val checklistItemsCompleted:Int,
    val checklistItemsTotal:Int,
    val notes: String
): DomainEntity()