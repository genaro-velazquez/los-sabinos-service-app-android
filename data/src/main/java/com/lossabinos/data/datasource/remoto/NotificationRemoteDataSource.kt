package com.lossabinos.data.datasource.remoto

import com.lossabinos.data.dto.repositories.retrofit.RetrofitResponseValidator
import com.lossabinos.data.dto.responses.GetNotificationsResponseDTO
import com.lossabinos.data.dto.utilities.HeadersMaker
import com.lossabinos.data.retrofit.NotificationsServices
import javax.inject.Inject

class NotificationRemoteDataSource @Inject constructor(
    private val notificationsServices: NotificationsServices,
    private val headersMaker: HeadersMaker
) {

    suspend fun getNotifications(
        page:Int,
        pageSize: Int
    ) : GetNotificationsResponseDTO{
        val response = notificationsServices.getNotifications(
            headers = headersMaker.build(),
            page = page,
            pageSize = pageSize
        )
        val json = RetrofitResponseValidator.validate(response = response)
        return GetNotificationsResponseDTO(json = json)
    }

}