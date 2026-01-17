package com.lossabinos.domain.repositories

import com.lossabinos.domain.responses.LoginResponse
import com.lossabinos.domain.valueobjects.Token

interface AuthenticationRepository {

    suspend fun loginWithEmailAndPassword(
        email : String,
        password: String
    ): LoginResponse

    suspend fun refreshToken(
        refreshToken: String
    ): Token

    fun getRefreshToken(): String

    fun getAccessToken(): String

}