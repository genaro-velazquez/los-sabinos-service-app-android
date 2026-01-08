package com.lossabinos.domain.valueobjects

import com.lossabinos.domain.entities.DomainEntity
import com.lossabinos.domain.entities.Metadata
import com.lossabinos.domain.entities.Observation
import kotlinx.serialization.Serializable


@Serializable
class Section(
    val name: String,
    val metadata: List<Metadata>,
    val optional: Boolean,
    val activities: List<Activity>,
    val observations: List<Observation>
): DomainEntity()