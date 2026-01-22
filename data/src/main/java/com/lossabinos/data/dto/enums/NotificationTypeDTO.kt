package com.lossabinos.data.dto.enums

import com.lossabinos.domain.enums.NotificationType

enum class NotificationTypeDTO(
    val entity: NotificationType
) {

    NEW_NOTIFICATION(entity = NotificationType.NEW_NOTIFICATION),
    UNKNOWN(entity = NotificationType.UNKNOWN);

    companion object {
        fun fromApi(value: String?): NotificationTypeDTO {
            return try {
                value
                    ?.uppercase()
                    ?.let { enumValueOf<NotificationTypeDTO>(it) }
                    ?: UNKNOWN
            } catch (e: Exception) {
                UNKNOWN
            }
        }
    }
}