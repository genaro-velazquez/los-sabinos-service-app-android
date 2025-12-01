package com.lossabinos.data.dto.entities

import com.lossabinos.data.dto.utilities.asInt
import com.lossabinos.data.dto.utilities.asJSONObject
import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.data.dto.valueobjects.VehiculeVersionDTO
import com.lossabinos.domain.entities.Vehicule
import org.json.JSONObject

class VehiculeDTO(
    val id:String,
    val vehiculeNumber:String,
    val licensePlate:String,
    val vehiculeVersion: VehiculeVersionDTO,
    val currentOdometerKm: Int
) : DTO<Vehicule>(){

    constructor(json: JSONObject) : this (
        id                  = json.asString("id"),
        vehiculeNumber      = json.asString("vehicle_number"),
        licensePlate        = json.asString("license_plate"),
        vehiculeVersion     = VehiculeVersionDTO(json = json.asJSONObject("model")),
        currentOdometerKm   = json.asInt("current_odometer_km")
    )

    override fun toEntity(): Vehicule = Vehicule(
        id                  = id,
        vehiculeNumber      = vehiculeNumber,
        licensePlate        = licensePlate,
        vehiculeVersion     = vehiculeVersion.toEntity(),
        currentOdometerKm   = currentOdometerKm
    )
}