package com.lossabinos.data.dto.valueobjects

import com.lossabinos.data.dto.entities.DTO
import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.domain.valueobjects.ServiceInfo
import org.json.JSONObject

class ServiceInfoDTO(
    val status: String,
    val actualStart: String,
    val scheduledEnd: String,
    val timeRemainingMinutes: String,
    val estimatedCompletion: String
): DTO<ServiceInfo>(){

    constructor(json: JSONObject) : this (
        status = json.asString("status"),
        actualStart = json.asString("actual_start"),
        scheduledEnd = json.asString("scheduled_end"),
        timeRemainingMinutes = json.asString("scheduled_end"),
        estimatedCompletion = json.asString("estimated_completion")
    )

    override fun toEntity(): ServiceInfo = ServiceInfo(
        status = status,
        actualStart = actualStart,
        scheduledEnd = scheduledEnd,
        timeRemainingMinutes = timeRemainingMinutes,
        estimatedCompletion = estimatedCompletion
    )

}