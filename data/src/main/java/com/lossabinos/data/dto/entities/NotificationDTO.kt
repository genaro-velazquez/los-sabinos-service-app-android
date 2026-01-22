package com.lossabinos.data.dto.entities

import com.lossabinos.data.dto.enums.NotificationCategoryDTO
import com.lossabinos.data.dto.enums.NotificationPriorityDTO
import com.lossabinos.data.dto.enums.NotificationTypeDTO
import com.lossabinos.data.dto.utilities.asBoolean
import com.lossabinos.data.dto.utilities.asJSONArray
import com.lossabinos.data.dto.utilities.asJSONObject
import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.data.dto.valueobjects.NotificationChannelsDTO
import com.lossabinos.domain.entities.Notification
import org.json.JSONObject

class NotificationDTO(
    val id: String,
    val type: NotificationTypeDTO,
    val category: NotificationCategoryDTO,
    val priority: NotificationPriorityDTO,
    val title: String,
    val message: String,
    val channels: NotificationChannelsDTO,
    val isRead: Boolean,
    val isArchived: Boolean,
    val isActive: Boolean,
    val createdAt: String,
    val actionUrl: String?,
    val actionLabel: String?,
    val isExpired: Boolean
) : DTO<Notification>(){

    constructor(json: JSONObject) : this (
        id = json.asString("id"),
        type = NotificationTypeDTO.fromApi(json.asString("notification_type")),
        category = NotificationCategoryDTO.fromApi(json.asString("category")),
        priority = NotificationPriorityDTO.fromApi(json.asString("priority")),
        title = json.asString("title"),
        message = json.asString("message"),
        channels = NotificationChannelsDTO(json = json.asJSONObject("channels")),
        isRead = json.asBoolean("is_read"),
        isArchived = json.asBoolean("is_archived"),
        isActive = json.asBoolean("is_active"),
        createdAt = json.asString("created_at"),
        actionUrl = json.asString("action_url"),
        actionLabel = json.asString("action_label"),
        isExpired = json.asBoolean("is_expired")

    )

    override fun toEntity(): Notification = Notification(
        id = id,
        type = type.entity,
        category = category.entity,
        priority = priority.entity,
        title = title,
        message = message,
        channels = channels.toEntity(),
        isRead = isRead,
        isArchived = isArchived,
        isActive = isActive,
        createdAt = createdAt,
        actionUrl = actionUrl,
        actionLabel = actionLabel,
        isExpired = isExpired
    )
}