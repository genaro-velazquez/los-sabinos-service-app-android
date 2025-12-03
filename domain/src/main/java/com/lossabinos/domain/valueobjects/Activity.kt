package com.lossabinos.domain.valueobjects

import com.lossabinos.domain.entities.DomainEntity

class Activity(
    val description: String,
    val requiresEvidence: Boolean
): DomainEntity()