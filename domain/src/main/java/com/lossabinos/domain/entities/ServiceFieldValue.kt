package com.lossabinos.domain.entities

import com.lossabinos.domain.valueobjects.FieldType


class ServiceFieldValue(
    val id: String,
    val label: String,
    val value: String?,
    val fieldType: FieldType,
    val required: Boolean
) : DomainEntity()
