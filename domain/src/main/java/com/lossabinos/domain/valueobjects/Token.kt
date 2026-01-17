package com.lossabinos.domain.valueobjects

import com.lossabinos.domain.entities.DomainEntity

class Token(
    val accessToken:String,
    val refreshToken: String,
    val tokenType:String,
    val expiresIn: Int
) : DomainEntity()