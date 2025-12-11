package com.lossabinos.domain.valueobjects

import com.lossabinos.domain.entities.DomainEntity
import kotlinx.serialization.Serializable


@Serializable
class Template(
    val name: String,
    val sections: List<Section>,
    val serviceFields:List<ServiceField>
): DomainEntity()