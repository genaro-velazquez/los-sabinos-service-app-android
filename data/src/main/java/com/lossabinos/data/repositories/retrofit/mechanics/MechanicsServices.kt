package com.lossabinos.data.repositories.retrofit.mechanics

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap

interface MechanicsServices {
    @GET("/api/v1/mechanics/me/assigned-services")
    suspend fun assignedServices(
        @HeaderMap headers: Map<String, String>
    ):  Response<String>

}