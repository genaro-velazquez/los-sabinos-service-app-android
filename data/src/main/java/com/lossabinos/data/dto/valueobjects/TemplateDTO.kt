package com.lossabinos.data.dto.valueobjects

import com.lossabinos.data.dto.entities.DTO
import com.lossabinos.data.dto.utilities.asJSONArray
import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.domain.valueobjects.Template
import org.json.JSONObject

class TemplateDTO(
    val name: String,
    val sections: List<SectionDTO>,
    val serviceFields:List<ServiceFieldDTO>
) : DTO<Template>(){

    constructor(json: JSONObject) : this (
        name = json.asString("name"),
        sections = json.asJSONArray("sections").let {
            array ->
            List(size = array.length()){
                index ->
                SectionDTO(json = array.getJSONObject(index))
            }
        },
        serviceFields = json.asJSONArray("service_fields").let {
            array ->
            List(size = array.length()){
                index ->
                ServiceFieldDTO(json = array.getJSONObject(index))
            }
        }
    )

    override fun toEntity(): Template = Template(
        name = name,
        sections = sections.map { it.toEntity() },
        serviceFields = serviceFields.map { it.toEntity() }
    )

}