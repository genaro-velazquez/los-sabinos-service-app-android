package com.lossabinos.data.retrofit

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SyncServices {

    @PUT("/api/v1/mechanics/services/{service_execution_id}/progress")
    suspend fun syncProgress(
        @HeaderMap headers: Map<String, String>,
        @Path("service_execution_id") serviceExecutionId: String,
        @Body request: RequestBody
    ): Response<String>

    @POST("/api/v1/mechanics/services/{service_execution_id}/items/{item_id}/photos")
    suspend fun syncPhotos(
        @HeaderMap headers: Map<String, String>,
        @Path("service_execution_id") serviceExecutionId: String,
        @Path("item_id") itemId: String,
        @Body request: RequestBody,
    ): Response<String>

}