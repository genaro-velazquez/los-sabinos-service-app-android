package com.lossabinos.domain.valueobjects

import com.lossabinos.domain.entities.DomainEntity

class ServiceInfo(
    val status: String,
    val actualStart: String,
    val scheduledEnd: String,
    val timeRemainingMinutes: String,
    val estimatedCompletion: String
): DomainEntity()