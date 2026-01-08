package com.lossabinos.data.dto.valueobjects

import com.lossabinos.data.dto.entities.DTO
import com.lossabinos.data.dto.entities.MetadataDTO
import com.lossabinos.data.dto.entities.ObservationDTO
import com.lossabinos.data.dto.utilities.asBoolean
import com.lossabinos.data.dto.utilities.asJSONArray
import com.lossabinos.data.dto.utilities.asJSONObject
import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.domain.valueobjects.Section
import org.json.JSONObject

class SectionDTO(
    val name: String,
    val metadata: List<MetadataDTO>,
    val optional: Boolean,
    val activities: List<ActivityDTO>,
    val observations: List<ObservationDTO>
): DTO<Section>(){

    constructor(json: JSONObject) : this (
        name = json.asString("name"),
        metadata = json.asJSONArray("metadata").let { array ->
            List(size = array.length(), init = {
                index ->
                MetadataDTO(json = array.getJSONObject(index))
            })
        },
        optional = json.asBoolean("optional"),
        activities = json.asJSONArray("activities").let { array ->
            List(size = array.length(), init = {
                index ->
                ActivityDTO(json = array.getJSONObject(index))
            })
        },
        observations = json.asJSONArray("observations").let { array ->
            List(size = array.length(), init = {
                index ->
                ObservationDTO(array.getJSONObject(index))
            })
        }
    )

    override fun toEntity(): Section = Section(
        name = name,
        metadata = metadata.map { it.toEntity() },
        optional = optional,
        activities = activities.map { it.toEntity() },
        observations = observations.map { it.toEntity() }
    )

}