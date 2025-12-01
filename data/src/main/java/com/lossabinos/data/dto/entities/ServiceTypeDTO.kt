package com.lossabinos.data.dto.entities

import com.lossabinos.data.dto.utilities.asInt
import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.domain.entities.Service
import com.lossabinos.domain.entities.ServiceType
import org.json.JSONObject

class ServiceTypeDTO(
    val id:String,
    val name: String,
    val code: String,
    val category: String,
    val estimatedDurationMinutes: Int
) : DTO<ServiceType>(){

    constructor(json: JSONObject) : this (
        id = json.asString("id"),
        name = json.asString("name"),
        code = json.asString("code"),
        category = json.asString("category"),
        estimatedDurationMinutes = json.asInt("estimated_duration_minutes")
    )

    override fun toEntity(): ServiceType = ServiceType(
        id = id,
        name = name,
        code = code,
        category = category,
        estimatedDurationMinutes = estimatedDurationMinutes
    )
}