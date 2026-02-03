package com.lossabinos.data.retrofit

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path


interface WorkRequestServices {

    @POST(value = "/api/v1/work-requests/")
    suspend fun workRequests(
        @HeaderMap headers: Map<String, String>,
        @Body request: RequestBody
    ): Response<String>

    @POST(value = "/api/v1/mechanics/services/{service_execution_id}/report-issue")
    suspend fun workRequestIssue(
        @HeaderMap headers: Map<String, String>,
        @Path("service_execution_id") serviceExecutionId: String,
        @Body request: RequestBody
    ): Response<String>

    @Multipart
    @POST(value = "/api/v1/mechanics/services/{service_execution_id}/report-issue/photo")
    suspend fun workRequestPhoto(
        @HeaderMap headers: Map<String, String>,
        @Path("service_execution_id") serviceExecutionId: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Response<String>
}