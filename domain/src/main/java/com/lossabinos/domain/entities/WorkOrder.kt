package com.lossabinos.domain.entities

class WorkOrder(
    val orderId: String,
    val orderNumber: String,
    val orderStatus:String,
    val priority:String,
    val vehicle: Vehicle,
    val zone: Zone,
    val scheduledDate:String,
    val scheduledEnd: String,
    val assignedServices: List<Service>
    ): DomainEntity()