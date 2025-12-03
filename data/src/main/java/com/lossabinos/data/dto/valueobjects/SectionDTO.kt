package com.lossabinos.data.dto.valueobjects

import com.lossabinos.data.dto.entities.DTO
import com.lossabinos.data.dto.utilities.asBoolean
import com.lossabinos.data.dto.utilities.asJSONArray
import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.domain.valueobjects.Section
import org.json.JSONObject

class SectionDTO(
    val name: String,
    val metadata: List<String>,
    val optional: Boolean,
    val activities: List<ActivityDTO>,
    val observations: List<String>
): DTO<Section>(){

    constructor(json: JSONObject) : this (
        name = json.asString("name"),
        metadata = emptyList(),
        optional = json.asBoolean("optional"),
        activities = json.asJSONArray("activities").let { array ->
            List(size = array.length()){
                index ->
                ActivityDTO(json = array.getJSONObject(index))
            }
        },
        observations = emptyList()
    )

    override fun toEntity(): Section = Section(
        name = name,
        metadata = metadata,
        optional = optional,
        activities = activities.map { it.toEntity() },
        observations = observations
    )

}