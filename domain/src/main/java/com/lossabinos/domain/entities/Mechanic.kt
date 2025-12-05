package com.lossabinos.domain.entities

class Mechanic(
    val id:String,
    val name:String,
    val email:String,
    val fullName: String,
    val rol: String,
    val zoneId:String,
    val zoneName: String
): DomainEntity()