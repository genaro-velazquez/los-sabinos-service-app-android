package com.lossabinos.data.datasource.remoto.interceptor

import com.lossabinos.data.datasource.local.TokenManager
import com.lossabinos.data.dto.repositories.retrofit.RetrofitResponseValidator
import com.lossabinos.data.dto.utilities.HeadersMaker
import com.lossabinos.data.dto.valueobjects.TokenDTO
import com.lossabinos.data.retrofit.AuthenticationServices
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import javax.inject.Inject

class TokenRefreshInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    private val authServices: AuthenticationServices,
    private val headersMaker: HeadersMaker
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // 1️⃣ Ejecutar request original
        val response = chain.proceed(originalRequest)

        if (response.code != 401) {
            return response
        }

        println("🔑 [Interceptor] 401 detectado, intentando refresh token")

        synchronized(this) {

            val currentAccessToken = tokenManager.getAccessToken()
            val requestToken = originalRequest.header("Authorization")
                ?.replace("Bearer ", "")
                ?: ""

            // 2️⃣ Si otro thread ya refrescó el token
            if (currentAccessToken.isNotBlank() && currentAccessToken != requestToken) {
                response.close()

                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $currentAccessToken")
                    .build()

                return chain.proceed(newRequest)
            }

            val refreshToken = tokenManager.getRefreshToken()

            if (refreshToken.isBlank()) {
                println("❌ [Interceptor] No hay refresh token")
                response.close()
                return response
            }

            // 3️⃣ Refresh síncrono
            return try {
                val newTokens = runBlocking {
                    val json = JSONObject()
                        .put("refreshToken", refreshToken)
                        .toString()
                        .toRequestBody("application/json".toMediaType())

                    val refreshResponse = authServices.refreshToken(
                        request = json,
                        headers = headersMaker.build()
                    )

                    val body = RetrofitResponseValidator.validate(refreshResponse)
                    TokenDTO(json = body).toEntity()
                }

                // 4️⃣ Guardar nuevos tokens
                tokenManager.saveTokens(
                    accessToken = newTokens.accessToken,
                    refreshToken = newTokens.refreshToken
                )

                response.close()

                // 5️⃣ Reintentar request
                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer ${newTokens.accessToken}")
                    .build()

                chain.proceed(newRequest)

            } catch (e: Exception) {
                println("❌ [Interceptor] Error refrescando token: ${e.message}")
                tokenManager.clearTokens()
                response.close()
                response
            }
        }
    }
}
