package com.lossabinos.data.dto.enums

import com.lossabinos.domain.enums.NotificationCategory

enum class NotificationCategoryDTO(
    val entity: NotificationCategory
) {
    SYSTEM(entity = NotificationCategory.SYSTEM),
    USER(entity = NotificationCategory.USER),
    UNKNOWN(entity = NotificationCategory.UNKNOWN);

    companion object {
        fun fromApi(value: String?): NotificationCategoryDTO {
            return try {
                value
                    ?.uppercase()
                    ?.let { enumValueOf<NotificationCategoryDTO>(it) }
                    ?: UNKNOWN
            } catch (e: Exception) {
                UNKNOWN
            }
        }
    }
}