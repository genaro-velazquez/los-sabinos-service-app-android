package com.lossabinos.domain.entities

import com.lossabinos.domain.valueobjects.FieldType


class ServiceFieldValue(
    val id: String,
    val assignedServiceId: String,  // FK (camelCase)
    val fieldIndex: Int,
    val fieldLabel: String,
    val fieldType: FieldType,
    val required: Boolean,
    val value: String? = null,
    val timestamp: String? = null
) : DomainEntity()
