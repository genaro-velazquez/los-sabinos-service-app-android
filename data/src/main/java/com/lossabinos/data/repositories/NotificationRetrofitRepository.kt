package com.lossabinos.data.repositories

import com.lossabinos.data.datasource.remoto.NotificationRemoteDataSource
import com.lossabinos.domain.enums.NotificationPriority
import com.lossabinos.domain.repositories.NotificationRepository
import com.lossabinos.domain.responses.GetNotificationsResponse

class NotificationRetrofitRepository(
    private val remoteDataSource: NotificationRemoteDataSource
) : NotificationRepository {

    override suspend fun getNotifications(
        page: Int,
        pageSize: Int
    ): GetNotificationsResponse {
        val dto = remoteDataSource.getNotifications(
            page = page,
            pageSize = pageSize
        )
        return dto.toEntity()
    }
}