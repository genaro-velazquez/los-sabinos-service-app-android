package com.lossabinos.domain.entities

class ExtraCost(
    val id:String,
    val assignedServiceId: String,
    val quantity: Double,
    val category: String,
    val description: String,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val syncStatus: String = "PENDING",
    val timestamp: Long = System.currentTimeMillis()
): DomainEntity()