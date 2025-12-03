package com.lossabinos.data.dto.entities

import com.lossabinos.data.dto.utilities.asInt
import com.lossabinos.data.dto.utilities.asJSONObject
import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.data.dto.valueobjects.VehicleVersionDTO
import com.lossabinos.domain.entities.Vehicle
import org.json.JSONObject

class VehicleDTO(
    val id:String,
    val vehicleNumber:String,
    val licensePlate:String,
    val vehicleVersion: VehicleVersionDTO,
    val currentOdometerKm: Int
) : DTO<Vehicle>(){

    constructor(json: JSONObject) : this (
        id                  = json.asString("id"),
        vehicleNumber       = json.asString("vehicle_number"),
        licensePlate        = json.asString("license_plate"),
        vehicleVersion      = VehicleVersionDTO(json = json.asJSONObject("model")),
        currentOdometerKm   = json.asInt("current_odometer_km")
    )

    override fun toEntity(): Vehicle = Vehicle(
        id                  = id,
        vehicleNumber      = vehicleNumber,
        licensePlate        = licensePlate,
        vehicleVersion      = vehicleVersion.toEntity(),
        currentOdometerKm   = currentOdometerKm
    )
}