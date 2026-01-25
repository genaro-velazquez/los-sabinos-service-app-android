package com.lossabinos.domain.usecases.notifications

import com.lossabinos.domain.repositories.NotificationRepository
import com.lossabinos.domain.responses.GetNotificationsResponse

class SetNotificationReadUseCase(
    private val repository: NotificationRepository
) {

    suspend operator fun invoke(
        idNotification:String
    ) {
        repository.setNotificationRead(
            idNotification = idNotification
        )
    }
}