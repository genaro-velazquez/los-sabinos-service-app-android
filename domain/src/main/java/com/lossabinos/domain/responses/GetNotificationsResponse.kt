package com.lossabinos.domain.responses

import com.lossabinos.domain.entities.DomainEntity
import com.lossabinos.domain.entities.Notification

class GetNotificationsResponse(
    val notifications: List<Notification>,
    val total: Int,
    val unreadCount: Int,
    val page: Int,
    val pageSize: Int
) : DomainEntity()