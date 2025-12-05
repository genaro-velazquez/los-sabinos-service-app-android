package com.lossabinos.data.dto.responses

import com.lossabinos.data.dto.entities.AssignedServiceDTO
import com.lossabinos.data.dto.entities.DTO
import com.lossabinos.data.dto.entities.MechanicDTO
import com.lossabinos.data.dto.entities.ServiceTypeDTO
import com.lossabinos.data.dto.utilities.asJSONArray
import com.lossabinos.data.dto.utilities.asJSONObject
import com.lossabinos.data.dto.valueobjects.SyncMetadataDTO
import com.lossabinos.domain.entities.AssignedService
import com.lossabinos.domain.responses.InitialDataResponse
import org.json.JSONObject

open class InitialDataResponseDTO(
    val mechanic: MechanicDTO,
    val assignedServices: List<AssignedServiceDTO>,
    val serviceTypes: List<ServiceTypeDTO>,
    val syncMetadata: SyncMetadataDTO
) : DTO<InitialDataResponse>(){

    constructor(json: JSONObject) : this (
        mechanic = MechanicDTO(json = json.asJSONObject("mechanic")),
        assignedServices = json.asJSONArray("assigned_services").let {
            array ->
            List(size = array.length()){
                index ->
                AssignedServiceDTO(json = array.getJSONObject(index))
            }
        },
        serviceTypes = json.asJSONArray("service_types").let {
            array ->
            List(size = array.length(), init = {
                index ->
                ServiceTypeDTO(json = array.getJSONObject(index))
            })
        },
        syncMetadata = SyncMetadataDTO(json = json.asJSONObject("sync_metadata"))
    )

    override fun toEntity(): InitialDataResponse = InitialDataResponse(
        mechanic = mechanic.toEntity(),
        assignedServices = assignedServices.map { it.toEntity() },
        serviceTypes = serviceTypes.map { it.toEntity() },
        syncMetadata = syncMetadata.toEntity()
    )

}