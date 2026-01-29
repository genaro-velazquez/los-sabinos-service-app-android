package com.lossabinos.data.retrofit

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST


interface WorkRequestServices {

    @POST(value = "/api/v1/work-requests/")
    suspend fun workRequests(
        @HeaderMap headers: Map<String, String>,
        @Body request: RequestBody
    ): Response<String>

}