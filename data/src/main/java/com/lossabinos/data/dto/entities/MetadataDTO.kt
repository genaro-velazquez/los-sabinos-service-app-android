package com.lossabinos.data.dto.entities

import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.domain.entities.Metadata
import org.json.JSONObject

class MetadataDTO(
    val id:String,
    val name: String,
    val value: String
) : DTO<Metadata>() {

    constructor(json: JSONObject) : this (
        id = json.asString("id"),
        name = json.asString("name"),
        value = json.asString("value")
    )

    override fun toEntity(): Metadata = Metadata(
        id = id,
        name = name,
        value = value
    )
}