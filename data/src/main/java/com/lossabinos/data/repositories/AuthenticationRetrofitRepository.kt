package com.lossabinos.data.dto.repositories.retrofit.authentication

import com.lossabinos.data.datasource.remoto.AuthenticationRemoteDataSource
import com.lossabinos.data.dto.repositories.retrofit.RetrofitResponseValidator
import com.lossabinos.data.dto.responses.LoginResponseDTO
import com.lossabinos.data.dto.utilities.HeadersMaker
import com.lossabinos.data.dto.valueobjects.TokenDTO
import com.lossabinos.data.retrofit.AuthenticationServices
import com.lossabinos.domain.repositories.AuthenticationRepository
import com.lossabinos.domain.repositories.UserPreferencesRepository
import com.lossabinos.domain.responses.LoginResponse
import com.lossabinos.domain.valueobjects.Token
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class AuthenticationRetrofitRepository(
    private val remoteDataSource: AuthenticationRemoteDataSource,
    private val userPreferencesRepository: UserPreferencesRepository
    ) : AuthenticationRepository{

    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): LoginResponse {

        val jsonObject = JSONObject().apply {
            put("email", email)
            put("password", password)
        }

        val requestBody = jsonObject.toString()
            .toRequestBody("application/json".toMediaType())

        val dto = remoteDataSource.loginWithEmailAndPassword(
            requestBody = requestBody
        )

        return dto.toEntity()
    }

    override suspend fun refreshToken(refreshToken: String): Token {

        val jsonObject = JSONObject().apply {
            put("refreshToken", refreshToken)
        }
        val requestBody = jsonObject.toString()
            .toRequestBody("application/json".toMediaType())
        val dto = remoteDataSource.refreshSession(requestBody = requestBody)
        return dto.toEntity()
    }

    override fun getRefreshToken(): String {
        return userPreferencesRepository.getRefreshToken()
    }

    override fun getAccessToken(): String {
        return userPreferencesRepository.getAccessToken()
    }
}