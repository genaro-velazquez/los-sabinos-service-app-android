package com.lossabinos.data.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path

interface MechanicsServices {
    @GET("/api/v1/mechanics/me/assigned-services")
    suspend fun assignedServices(
        @HeaderMap headers: Map<String, String>
    ): Response<String>

    @GET("/api/v1/mechanics/services/{id_service}/template")
    suspend fun detailedService(
        @HeaderMap headers: Map<String, String>,
        @Path("id_service") idService: String
    ): Response<String>

    @GET("/api/v1/sync/initial-data")
    suspend fun syncInitialData(
        @HeaderMap headers: Map<String, String>
    ): Response<String>

    @GET("/api/v1/vehicles/{id_vehicle}/qr")
    suspend fun getVehicleByQR(
        @HeaderMap headers: Map<String, String>,
        @Path("id_vehicle") idVehicle:String
    ): Response<String>
}