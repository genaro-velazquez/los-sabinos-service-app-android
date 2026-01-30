package com.lossabinos.data.dto.entities

import com.lossabinos.data.dto.utilities.asInt
import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.domain.entities.Photo
import org.json.JSONObject

class PhotoDTO(
    val id:String,
    val url: String,
    val description: String,
    val uploadedAt: String
) : DTO<Photo>(){

    constructor(json: JSONObject): this (
        id =            json.asString("id"),
        url =           json.asString("service_execution_id"),
        description =   json.asString("checklist_item_id"),
        uploadedAt =    json.asString("file_url")
    )

    override fun toEntity(): Photo = Photo(
        id =            id,
        url =           url,
        description =   description,
        uploadedAt =    uploadedAt
    )
}