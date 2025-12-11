package com.lossabinos.domain.valueobjects

import com.lossabinos.domain.entities.DomainEntity
import kotlinx.serialization.Serializable

@Serializable
class ChecklistTemplate(
    val name:String,
    val version:String,
    val template: Template
) : DomainEntity()