package com.lossabinos.data.dto.valueobjects

import com.lossabinos.data.dto.entities.DTO
import com.lossabinos.data.dto.utilities.asInt
import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.domain.valueobjects.CurrentProgress
import org.json.JSONObject

class CurrentProgressDTO(
    val itemsCompleted:Int,
    val itemTotal: Int,
    val progressPercentage: Int,
    val lastUpdated: String
): DTO<CurrentProgress>(){

    constructor(json: JSONObject):this(
        itemsCompleted = json.asInt("items_completed"),
        itemTotal = json.asInt("items_total"),
        progressPercentage = json.asInt("progress_percentage"),
        lastUpdated = json.asString("last_updated")
    )

    override fun toEntity(): CurrentProgress = CurrentProgress(
        itemsCompleted = itemsCompleted,
        itemTotal = itemTotal,
        progressPercentage = progressPercentage,
        lastUpdated = lastUpdated
    )

}