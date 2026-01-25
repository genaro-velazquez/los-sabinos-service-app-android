package com.lossabinos.data.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationsServices  {

    @GET("/api/v1/notifications/")
    suspend fun getNotifications(
        @HeaderMap headers: Map<String, String>,
        @Query("page") page: Int,
        @Query("size") pageSize: Int
    ): Response<String>

    @GET("/api/v1/notifications/unread-count")
    suspend fun getUnreadCountNotifications(
        @HeaderMap headers: Map<String, String>
    ): Response<String>

    @PUT("/api/v1/notifications/{notification_id}/read")
    suspend fun setNotificationRead(
        @HeaderMap headers: Map<String, String>,
        @Path("notification_id") idNotification: String
    ): Response<String>

}