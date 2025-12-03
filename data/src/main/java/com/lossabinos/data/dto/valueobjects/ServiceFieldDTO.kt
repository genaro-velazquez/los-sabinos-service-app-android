package com.lossabinos.data.dto.valueobjects

import com.lossabinos.data.dto.entities.DTO
import com.lossabinos.data.dto.utilities.asBoolean
import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.domain.valueobjects.ServiceField
import org.json.JSONObject

class ServiceFieldDTO (
    val type: String,
    val label: String,
    val required: Boolean
): DTO<ServiceField>(){

    constructor(json: JSONObject) : this (
        type = json.asString("type"),
        label = json.asString("label"),
        required = json.asBoolean("required")
    )

    override fun toEntity(): ServiceField = ServiceField(
        type = type,
        label = label,
        required = required
    )

}