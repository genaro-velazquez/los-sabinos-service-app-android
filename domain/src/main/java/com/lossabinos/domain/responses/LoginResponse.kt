package com.lossabinos.domain.responses

import com.lossabinos.domain.entities.User
import com.lossabinos.domain.entities.DomainEntity

class LoginResponse(
    val primaryColor:   String,
    val secondaryColor: String,
    val appName:        String,
    val accessToken:    String,
    val refreshToken:   String,
    val client:         User
): DomainEntity()
