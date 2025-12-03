package com.lossabinos.domain.valueobjects

import com.lossabinos.domain.entities.DomainEntity

class Template(
    val name: String,
    val sections: List<Section>,
    val serviceFields:List<ServiceField>
): DomainEntity()