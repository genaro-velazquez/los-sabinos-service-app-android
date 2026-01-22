package com.lossabinos.serviceapp.mappers

import com.lossabinos.domain.entities.Notification
import com.lossabinos.serviceapp.ui.components.molecules.NotificationItemUIModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object NotificationMapper {

    fun Notification.toUIModel(): NotificationItemUIModel {
        return NotificationItemUIModel(
            id = this.id,
            title = this.title,
            message = this.message,
            priority = this.priority,
            timestamp = convertIso8601ToTimestamp(this.createdAt),
            isRead = this.isRead,
            type = this.type,
            category = this.category,
            actionUrl = this.actionUrl
        )
    }

    fun List<Notification>.toUIModels(): List<NotificationItemUIModel> {
        return this.map { it.toUIModel() }
    }

    private fun convertIso8601ToTimestamp(dateString: String): Long {
        return try {
            val formatter = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
                Locale.getDefault()
            )
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            val date = formatter.parse(dateString)
            date?.time ?: System.currentTimeMillis()
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }
}