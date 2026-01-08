package com.lossabinos.domain.entities

import kotlinx.serialization.Serializable

@Serializable
class Metadata(
    val id:String,
    val name: String,
    val value: String
): DomainEntity()