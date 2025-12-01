package com.lossabinos.data.dto.entities

import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.domain.entities.Mechanic
import org.json.JSONObject

class MechanicDTO(
    val id:String,
    val fullName: String,
    val rol: String,
    val zoneId:String,
    val zoneName: String
) : DTO<Mechanic>(){

    constructor(json: JSONObject) : this (
        id = json.asString("id"),
        fullName = json.asString("full_name"),
        rol = json.asString("role"),
        zoneId = json.asString("zone_id"),
        zoneName = json.asString("zone_name")
    )

    override fun toEntity(): Mechanic = Mechanic(
        id = id,
        fullName = fullName,
        rol = rol,
        zoneId = zoneId,
        zoneName = zoneName
    )


}