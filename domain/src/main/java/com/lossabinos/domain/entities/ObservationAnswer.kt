package com.lossabinos.domain.entities

class ObservationAnswer(
    val id: Long,
    val assignedServiceId: String,
    val sectionIndex: Int,
    val observationIndex: Int,
    val observationDescription: String,
    val answer: String?,
    val timestamp: String?
) : DomainEntity()
