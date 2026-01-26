package com.lossabinos.data.retrofit

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface SyncServices {

    @PUT("/api/v1/mechanics/services/{service_execution_id}/progress")
    suspend fun syncProgress(
        @HeaderMap headers: Map<String, String>,
        @Path("service_execution_id") serviceExecutionId: String,
        @Body request: RequestBody
    ): Response<String>

    @Multipart
    @POST("/api/v1/mechanics/services/{service_execution_id}/items/{item_id}/photos")
    suspend fun syncPhotos(
        @HeaderMap headers: Map<String, String>,
        @Path("service_execution_id") serviceExecutionId: String,
        @Path("item_id") itemId: String,
        @Part file: MultipartBody.Part,
        @Part("photo_type") photoType: RequestBody,
        @Part("description") description: RequestBody
    ): Response<String>

    @POST("/api/v1/work-orders/services/{service_execution_id}/sign")
    suspend fun singChecklist(
        @HeaderMap headers: Map<String, String>,
        @Path("service_execution_id") serviceExecutionId: String,
        @Body request: RequestBody
    ): Response<String>

    @POST("/api/v1/mechanics/services/{service_execution_id}/report-extra-costs")
    suspend fun reportExtraCost(
        @HeaderMap headers: Map<String, String>,
        @Path(value = "service_execution_id") idServiceExecution: String,
        @Body request: RequestBody
    ):Response<String>
}