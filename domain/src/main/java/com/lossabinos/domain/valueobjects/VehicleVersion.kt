package com.lossabinos.domain.valueobjects

import com.lossabinos.domain.entities.DomainEntity

class VehicleVersion(
    val make:String,
    val model:String,
    val year: Int
): DomainEntity()