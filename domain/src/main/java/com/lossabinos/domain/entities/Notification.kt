package com.lossabinos.domain.entities

import com.lossabinos.domain.enums.NotificationCategory
import com.lossabinos.domain.enums.NotificationPriority
import com.lossabinos.domain.enums.NotificationType
import com.lossabinos.domain.valueobjects.NotificationChannels

class Notification (
    val id: String,
    val type: NotificationType,
    val category: NotificationCategory,
    val priority: NotificationPriority,
    val title: String,
    val message: String,
    val channels: NotificationChannels,
    val isRead: Boolean,
    val isArchived: Boolean,
    val isActive: Boolean,
    val createdAt: String,
    val actionUrl: String?,
    val actionLabel: String?,
    val isExpired: Boolean
) : DomainEntity()