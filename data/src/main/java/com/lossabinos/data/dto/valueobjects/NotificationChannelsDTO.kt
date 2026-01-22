package com.lossabinos.data.dto.valueobjects

import com.lossabinos.data.dto.entities.DTO
import com.lossabinos.data.dto.utilities.asJSONObject
import com.lossabinos.domain.valueobjects.NotificationChannels
import org.json.JSONObject

class NotificationChannelsDTO(
    val sms: NotificationChannelDTO,
    val push: NotificationChannelDTO,
    val email: NotificationChannelDTO,
    val inApp: NotificationChannelDTO
): DTO<NotificationChannels>(){

    constructor(json: JSONObject) : this (
        sms = NotificationChannelDTO(json = json.asJSONObject("sms")),
        push = NotificationChannelDTO(json = json.asJSONObject("push")),
        email = NotificationChannelDTO(json = json.asJSONObject("email")),
        inApp = NotificationChannelDTO(json = json.asJSONObject("in_app"))
    )

    override fun toEntity(): NotificationChannels = NotificationChannels(
        sms = sms.toEntity(),
        push = push.toEntity(),
        email = email.toEntity(),
        inApp = inApp.toEntity()
    )
}