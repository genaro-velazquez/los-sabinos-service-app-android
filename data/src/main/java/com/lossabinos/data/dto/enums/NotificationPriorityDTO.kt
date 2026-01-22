package com.lossabinos.data.dto.enums

import com.lossabinos.domain.enums.NotificationPriority

enum class NotificationPriorityDTO(
    val entity: NotificationPriority
) {

    LOW(entity = NotificationPriority.LOW),
    NORMAL(entity = NotificationPriority.NORMAL),
    HIGH(entity = NotificationPriority.HIGH),
    UNKNOWN(entity = NotificationPriority.UNKNOWN);

    companion object {
        fun fromApi(value: String?): NotificationPriorityDTO {
            return try {
                value
                    ?.uppercase()
                    ?.let { enumValueOf<NotificationPriorityDTO>(it) }
                    ?: UNKNOWN
            } catch (e: Exception) {
                UNKNOWN
            }
        }
    }


}