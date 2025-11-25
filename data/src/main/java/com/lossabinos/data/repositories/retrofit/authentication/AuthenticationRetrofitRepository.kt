package com.lossabinos.data.dto.repositories.retrofit.authentication

import com.lossabinos.data.dto.repositories.retrofit.RetrofitResponseValidator
import com.lossabinos.data.dto.responses.LoginResponseDTO
import com.lossabinos.data.dto.utilities.HeadersMaker
import com.lossabinos.domain.repositories.AuthenticationRepository
import com.lossabinos.domain.responses.LoginResponse

class AuthenticationRetrofitRepository(
    private val authenticationServices: AuthenticationServices,
    private val headersMaker: HeadersMaker
) : AuthenticationRepository{

    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): LoginResponse {
        val body = HashMap<String, String>()
        body["email"] = email
        body["password"] = password

        val response = authenticationServices.login(body = body, headers = headersMaker.build())
        val json = RetrofitResponseValidator.validate(response = response)
        val dto = LoginResponseDTO(json = json)
        return dto.toEntity()
    }
}