package com.lossabinos.data.dto.repositories.retrofit.authentication

import com.lossabinos.data.dto.repositories.retrofit.RetrofitResponseValidator
import com.lossabinos.data.dto.responses.LoginResponseDTO
import com.lossabinos.data.dto.utilities.HeadersMaker
import com.lossabinos.data.repositories.retrofit.authentication.AuthenticationServices
import com.lossabinos.domain.repositories.AuthenticationRepository
import com.lossabinos.domain.responses.LoginResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class AuthenticationRetrofitRepository(
    private val authenticationServices: AuthenticationServices,
    private val headersMaker: HeadersMaker
) : AuthenticationRepository{

    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): LoginResponse {
        /*
        val body = HashMap<String, String>()
        body["email"] = email
        body["password"] =
        */

        val jsonObject = JSONObject().apply {
            put("email", email)
            put("password", password)
        }

        val requestBody = jsonObject.toString()
            .toRequestBody("application/json".toMediaType())

        val response = authenticationServices.login(request = requestBody, headers = headersMaker.build())
        val json = RetrofitResponseValidator.validate(response = response)
        val dto = LoginResponseDTO(json = json)
        return dto.toEntity()
    }
}