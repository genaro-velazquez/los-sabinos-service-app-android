package com.lossabinos.data.dto.valueobjects

import com.lossabinos.data.dto.entities.DTO
import com.lossabinos.data.dto.utilities.asInt
import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.domain.valueobjects.VehiculeVersion
import org.json.JSONObject

class VehiculeVersionDTO(
    val make:String,
    val model:String,
    val year: Int
): DTO<VehiculeVersion>(){

    constructor(json: JSONObject) : this (
        make = json.asString("make"),
        model = json.asString("model"),
        year = json.asInt("year")
    )

    override fun toEntity(): VehiculeVersion = VehiculeVersion(
        make = make,
        model = model,
        year = year
    )
}