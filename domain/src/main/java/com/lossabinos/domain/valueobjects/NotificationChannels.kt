package com.lossabinos.domain.valueobjects

import com.lossabinos.domain.entities.DomainEntity

class NotificationChannels(
    val sms: NotificationChannel,
    val push: NotificationChannel,
    val email: NotificationChannel,
    val inApp: NotificationChannel
): DomainEntity()