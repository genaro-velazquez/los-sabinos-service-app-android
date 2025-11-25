package com.lossabinos.data.repositories.retrofit.authentication

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface AuthenticationServices {

    @POST("/api/v1/auth/login")
    suspend fun login(
        @Body request: RequestBody,
        @HeaderMap headers: Map<String, String>
    ):  Response<String>

/*
    @POST("/api/v1/auth/login")
    @FormUrlEncoded
    suspend fun login(
        @FieldMap body: Map<String, String>,
        @HeaderMap headers: Map<String, String>
    ):  Response<String>
*/
}