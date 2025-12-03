package com.lossabinos.data.dto.valueobjects

import com.lossabinos.data.dto.entities.DTO
import com.lossabinos.data.dto.utilities.asInt
import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.domain.valueobjects.VehicleVersion
import org.json.JSONObject

class VehicleVersionDTO(
    val make:String,
    val model:String,
    val year: Int
): DTO<VehicleVersion>(){

    constructor(json: JSONObject) : this (
        make = json.asString("make"),
        model = json.asString("model"),
        year = json.asInt("year")
    )

    override fun toEntity(): VehicleVersion = VehicleVersion(
        make = make,
        model = model,
        year = year
    )
}