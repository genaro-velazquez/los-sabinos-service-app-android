package com.lossabinos.domain.entities

import com.lossabinos.domain.valueobjects.VehiculeVersion

class Vehicule(
    val id:String,
    val vehiculeNumber:String,
    val licensePlate:String,
    val vehiculeVersion: VehiculeVersion,
    val currentOdometerKm: Int
): DomainEntity()