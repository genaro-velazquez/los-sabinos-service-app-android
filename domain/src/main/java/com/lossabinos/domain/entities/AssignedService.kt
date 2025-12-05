package com.lossabinos.domain.entities

import com.lossabinos.domain.valueobjects.Template

class AssignedService(
    val id:String,
    val workOrderId: String,
    val workOrderNumber:String,
    val serviceTypeId:String,
    val serviceTypeName:String,
    val vehicle: Vehicle,
    val status: String,
    val scheduledStart: String,
    val scheduledEnd: String,
    val priority: String,
    val template: Template
): DomainEntity()