package com.lossabinos.data.dto.entities

import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.domain.entities.Zone
import org.json.JSONObject

class ZoneDTO(
    val id: String,
    val name: String,
    val code: String
) : DTO<Zone>(){

    constructor(json: JSONObject) : this (
        id      = json.asString("id"),
        name    = json.asString("name"),
        code    = json.asString("code")
    )

    override fun toEntity(): Zone = Zone(
        id      = id,
        name    = name,
        code    = code
    )
}