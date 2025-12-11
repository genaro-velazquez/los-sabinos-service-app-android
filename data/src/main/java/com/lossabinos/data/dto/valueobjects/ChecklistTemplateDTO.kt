package com.lossabinos.data.dto.valueobjects

import com.lossabinos.data.dto.entities.DTO
import com.lossabinos.data.dto.utilities.asJSONObject
import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.domain.valueobjects.ChecklistTemplate
import com.lossabinos.domain.valueobjects.Template
import org.json.JSONObject

class ChecklistTemplateDTO(
    val name:String,
    val version:String,
    val template: TemplateDTO
) : DTO<ChecklistTemplate>(){

    constructor(json: JSONObject) : this (
        name = json.asString("name"),
        version = json.asString("version"),
        template = TemplateDTO(json = json.asJSONObject("template_data"))
    )

    override fun toEntity(): ChecklistTemplate = ChecklistTemplate(
        name = name,
        version = version,
        template = template.toEntity()
    )

}