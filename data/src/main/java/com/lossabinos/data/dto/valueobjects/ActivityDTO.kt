package com.lossabinos.data.dto.valueobjects

import com.lossabinos.data.dto.entities.DTO
import com.lossabinos.data.dto.utilities.asBoolean
import com.lossabinos.data.dto.utilities.asJSONObject
import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.domain.valueobjects.Activity
import org.json.JSONObject

class ActivityDTO(
    val description: String,
    val requiresEvidence: Boolean
): DTO<Activity>(){

    constructor(json: JSONObject) : this (
        description = json.asString("description"),
        requiresEvidence = json.asBoolean("requiresEvidence")
    )

    override fun toEntity(): Activity = Activity(
        description = description,
        requiresEvidence = requiresEvidence
    )
}