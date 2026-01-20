package com.lossabinos.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
sealed class LocationEvent {
    @Serializable
    @SerialName("idle")
    object Idle : LocationEvent()

    @Serializable
    @SerialName("connected")
    data class Connected(
        @SerialName("user_id") val userId: String,
        @SerialName("tenant_id") val tenantId: String,
        val message: String? = null
    ) : LocationEvent()

    @Serializable
    @SerialName("pong")
    object Pong : LocationEvent()

    @Serializable
    @SerialName("error")
    data class Error(val message: String) : LocationEvent()

    @Serializable
    @SerialName("maintenance_alert")
    data class MaintenanceAlert(
        val data: MaintenanceAlertData,
        val timestamp: String
    ) : LocationEvent()

    @Serializable
    @SerialName("new_notification")
    data class NewNotification(
        val data: NotificationWrapper,
        val timestamp: String
    ) : LocationEvent()

    @Serializable
    @SerialName("location_update")
    data class LocationUpdate(val data: LocationData) : LocationEvent()
}

@Serializable
data class MaintenanceAlertData(
    val id: String,
    @SerialName("vehicle_id") val vehicleId: String,
    @SerialName("alert_type") val alertType: String,
    val severity: String,
    val title: String,
    val message: String,
    @SerialName("km_until_due") val kmUntilDue: Int? = null,
    @SerialName("created_at") val createdAt: String
)

@Serializable
data class NotificationWrapper(
    val notification: AppNotification
)

@Serializable
data class AppNotification(
    val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("tenant_id") val tenantId: String,
    @SerialName("notification_type") val notificationType: String,
    val title: String,
    val message: String,
    val category: String? = null,
    val priority: String? = null,
    @SerialName("is_read") val isRead: Boolean,
    @SerialName("is_archived") val isArchived: Boolean,
    @SerialName("read_at") val readAt: String? = null,
    @SerialName("archived_at") val archivedAt: String? = null,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String? = null,
    @SerialName("reference_type") val referenceType: String? = null,
    @SerialName("reference_id") val referenceId: String? = null,
    @SerialName("action_url") val actionUrl: String? = null,
    @SerialName("action_label") val actionLabel: String? = null,
    @SerialName("expires_at") val expiresAt: String? = null,
    @SerialName("notification_metadata") val metadata: JsonElement? = null,
    val channels: JsonElement? = null
)
