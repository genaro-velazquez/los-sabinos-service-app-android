package com.lossabinos.data.dto.valueobjects

import com.lossabinos.data.dto.entities.DTO
import com.lossabinos.data.dto.utilities.asInt
import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.domain.valueobjects.SyncMetadata
import org.json.JSONObject

class SyncMetadataDTO(
    val serverTimestamp:String,
    val totalServices: Int,
    val pendingServices:Int,
    val inProgressServices:Int,
    val lastSync: String
): DTO<SyncMetadata>(){

    constructor(json: JSONObject) : this (
        serverTimestamp = json.asString("server_timestamp"),
        totalServices = json.asInt("total_services"),
        pendingServices = json.asInt("pending_services"),
        inProgressServices = json.asInt("in_progress_services"),
        lastSync = json.asString("last_sync")
    )

    override fun toEntity(): SyncMetadata  = SyncMetadata(
        serverTimestamp = serverTimestamp,
        totalServices = totalServices,
        pendingServices = pendingServices,
        inProgressServices = inProgressServices,
        lastSync = lastSync
    )

}