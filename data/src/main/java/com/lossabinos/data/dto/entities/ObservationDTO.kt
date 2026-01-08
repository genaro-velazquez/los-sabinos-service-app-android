package com.lossabinos.data.dto.entities

import com.lossabinos.data.dto.enums.ResponseTypeDTO
import com.lossabinos.data.dto.utilities.asBoolean
import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.domain.entities.Observation
import com.lossabinos.domain.enums.ResponseType
import org.json.JSONObject

class ObservationDTO(
    val id: String,
    val description: String,
    val responseType: ResponseTypeDTO,
    val requiresResponse: Boolean
): DTO<Observation>(){

    constructor(json: JSONObject) : this (
        id = json.asString("id"),
        description = json.asString("description"),
        responseType = ResponseTypeDTO.fromApi(value = json.asString("response_type")),
        requiresResponse = json.asBoolean("requiresResponse")
    )

    override fun toEntity(): Observation = Observation(
        id = id,
        description = description,
        responseType = responseType.entity,
        requiresResponse = requiresResponse
    )
}