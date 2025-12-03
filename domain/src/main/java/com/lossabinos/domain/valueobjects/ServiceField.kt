package com.lossabinos.domain.valueobjects

import com.lossabinos.domain.entities.DomainEntity

class ServiceField(
    val type: String,
    val label: String,
    val required: Boolean
): DomainEntity()