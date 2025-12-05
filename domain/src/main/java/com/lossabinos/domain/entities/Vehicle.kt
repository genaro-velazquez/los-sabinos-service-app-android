package com.lossabinos.domain.entities

import com.lossabinos.domain.valueobjects.VehicleVersion

class Vehicle(
    val id:String,
    val vin: String,
    val economicNumber: String,
    val modelName: String,
    val vehicleNumber:String,
    val licensePlate:String,
    val vehicleVersion: VehicleVersion,
    val currentOdometerKm: Int
): DomainEntity()