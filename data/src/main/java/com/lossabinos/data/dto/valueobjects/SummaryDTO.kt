package com.lossabinos.data.dto.valueobjects

import com.lossabinos.data.dto.entities.DTO
import com.lossabinos.data.dto.utilities.asInt
import com.lossabinos.domain.valueobjects.Summary
import org.json.JSONObject

class SummaryDTO(
    val totalService:Int,
    val pending:Int,
    val inProgress:Int,
    val completed: Int,
    val todayServices:Int
) : DTO<Summary>(){

    constructor(json: JSONObject) : this (
        totalService = json.asInt("total_services"),
        pending = json.asInt("pending"),
        inProgress = json.asInt("in_progress"),
        completed = json.asInt("completed"),
        todayServices = json.asInt("today_services")
    )

    override fun toEntity(): Summary = Summary(
        totalService = totalService,
        pending = pending,
        inProgress = inProgress,
        completed = completed,
        todayServices = todayServices
    )
}