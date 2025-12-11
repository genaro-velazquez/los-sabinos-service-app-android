package com.lossabinos.domain.valueobjects

import com.lossabinos.domain.entities.DomainEntity
import kotlinx.serialization.Serializable

@Serializable
class ServiceField(
    val type: String,
    val label: String,
    val required: Boolean
): DomainEntity()