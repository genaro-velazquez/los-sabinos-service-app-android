package com.lossabinos.domain.valueobjects

import com.lossabinos.domain.entities.DomainEntity

class NotificationChannel(
    val enabled: Boolean
) : DomainEntity()