package com.lossabinos.domain.repositories

import com.lossabinos.domain.responses.GetNotificationsResponse

interface NotificationRepository {
    suspend fun getNotifications(
        page: Int,
        pageSize: Int
    ): GetNotificationsResponse

    suspend fun setNotificationRead(
        idNotification:String
    )
}