package com.lossabinos.domain.repositories

import com.lossabinos.domain.responses.LoginResponse

interface AuthenticationRepository {

    suspend fun loginWithEmailAndPassword(
        email : String,
        password: String
    ): LoginResponse

}