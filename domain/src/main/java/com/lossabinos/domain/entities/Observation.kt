package com.lossabinos.domain.entities

import com.lossabinos.domain.enums.ResponseType
import kotlinx.serialization.Serializable

@Serializable
class Observation(
    val id: String,
    val description: String,
    val responseType: ResponseType,
    val requiresResponse: Boolean
): DomainEntity()