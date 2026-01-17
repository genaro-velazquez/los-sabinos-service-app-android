package com.lossabinos.data.datasource.remoto

import com.lossabinos.data.dto.repositories.retrofit.RetrofitResponseValidator
import com.lossabinos.data.dto.responses.LoginResponseDTO
import com.lossabinos.data.dto.utilities.HeadersMaker
import com.lossabinos.data.dto.valueobjects.TokenDTO
import com.lossabinos.data.retrofit.AuthenticationServices
import com.lossabinos.domain.valueobjects.Token
import okhttp3.RequestBody
import javax.inject.Inject

class AuthenticationRemoteDataSource @Inject constructor(
    private val authenticationServices: AuthenticationServices,
    private val headersMaker: HeadersMaker
) {

    suspend fun loginWithEmailAndPassword(
        requestBody: RequestBody
    ): LoginResponseDTO{
        val response = authenticationServices.login(
            request = requestBody,
            headers = headersMaker.build()
        )
        val json = RetrofitResponseValidator.validate(response = response)
        return LoginResponseDTO(json = json)
    }

    suspend fun refreshSession(requestBody: RequestBody): TokenDTO{
        val response = authenticationServices.refreshToken(
            request = requestBody,
            headers = headersMaker.build()
        )
        val json = RetrofitResponseValidator.validate(response = response)
        return TokenDTO(json = json)
    }

}