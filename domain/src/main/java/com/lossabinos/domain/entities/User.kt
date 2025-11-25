package com.lossabinos.domain.entities

class User (
    val id:String,
    val firstName: String,
    val lastName:String,
    val email:String,
    val isAdmin: Boolean,
    val userRol: UserRol
): DomainEntity()