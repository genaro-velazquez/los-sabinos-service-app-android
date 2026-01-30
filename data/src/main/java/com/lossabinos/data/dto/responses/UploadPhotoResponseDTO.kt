package com.lossabinos.data.dto.responses

import com.lossabinos.data.dto.entities.DTO
import com.lossabinos.data.dto.entities.PhotoDTO
import com.lossabinos.data.dto.utilities.asJSONObject
import com.lossabinos.domain.responses.UploadPhotoResponse
import org.json.JSONObject

class UploadPhotoResponseDTO(
    val photo: PhotoDTO
): DTO<UploadPhotoResponse>(){

    constructor(json: JSONObject) : this (
        photo = PhotoDTO(json = json.asJSONObject("data").asJSONObject("photo"))
    )

    override fun toEntity(): UploadPhotoResponse = UploadPhotoResponse(
        photo = photo.toEntity()
    )
}