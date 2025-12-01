package com.lossabinos.domain.entities

class ServiceType(
    val id:String,
    val name: String,
    val code: String,
    val category: String,
    val estimatedDurationMinutes: Int
): DomainEntity()