package com.lossabinos.domain.valueobjects

import com.lossabinos.domain.entities.DomainEntity
import kotlinx.serialization.Serializable

@Serializable
class Activity(
    val id:String,
    val description: String,
    val requiresEvidence: Boolean
): DomainEntity()