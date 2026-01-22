package com.lossabinos.domain.usecases.notifications

import com.lossabinos.domain.repositories.NotificationRepository
import com.lossabinos.domain.responses.GetNotificationsResponse

class GetNotificationsUseCase(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(
        page: Int,
        pageSize: Int
    ): GetNotificationsResponse{
        return repository.getNotifications(
            page = page ,
            pageSize = pageSize,
        )
    }
}