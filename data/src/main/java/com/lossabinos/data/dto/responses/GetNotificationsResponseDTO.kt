package com.lossabinos.data.dto.responses

import com.lossabinos.data.dto.entities.DTO
import com.lossabinos.data.dto.entities.NotificationDTO
import com.lossabinos.data.dto.utilities.asInt
import com.lossabinos.data.dto.utilities.asJSONArray
import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.domain.responses.GetNotificationsResponse
import org.json.JSONObject

class GetNotificationsResponseDTO(
    val notifications: List<NotificationDTO>,
    val total: Int,
    val unreadCount: Int,
    val page: Int,
    val pageSize: Int
) : DTO<GetNotificationsResponse>() {

    constructor(json: JSONObject) : this (
        notifications = json.asJSONArray("items").let { array ->
            List(size = array.length(), init = {
                index ->
                NotificationDTO(json = array.getJSONObject(index))
            })
        },
        total = json.asInt("total"),
        unreadCount = json.asInt("unread_count"),
        page = json.asInt("page"),
        pageSize = json.asInt("page_size")
    )

    override fun toEntity(): GetNotificationsResponse = GetNotificationsResponse(
        notifications = notifications.map { it.toEntity() },
        total = total,
        unreadCount = unreadCount,
        page = page,
        pageSize = pageSize
    )
}