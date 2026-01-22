package com.lossabinos.data.dto.valueobjects

import com.lossabinos.data.dto.entities.DTO
import com.lossabinos.data.dto.utilities.asBoolean
import com.lossabinos.domain.entities.Notification
import com.lossabinos.domain.valueobjects.NotificationChannel
import org.json.JSONObject

class NotificationChannelDTO(
    val enabled: Boolean
): DTO<NotificationChannel>(){

    constructor(json: JSONObject): this (
        enabled = json.asBoolean("enabled")
    )

    override fun toEntity(): NotificationChannel = NotificationChannel(
        enabled = enabled
    )
}