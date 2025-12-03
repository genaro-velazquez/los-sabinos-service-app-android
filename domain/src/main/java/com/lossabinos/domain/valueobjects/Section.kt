package com.lossabinos.domain.valueobjects

import com.lossabinos.domain.entities.DomainEntity

class Section(
    val name: String,
    val metadata: List<String>,
    val optional: Boolean,
    val activities: List<Activity>,
    val observations: List<String>
): DomainEntity()