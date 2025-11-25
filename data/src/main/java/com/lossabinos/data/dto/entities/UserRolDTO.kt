package com.lossabinos.data.dto.entities

import com.lossabinos.data.dto.utilities.asJSONArray
import com.lossabinos.data.dto.utilities.asJSONObject
import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.data.dto.valueobjects.UserPermissionsDTO
import com.lossabinos.domain.entities.UserRol
import org.json.JSONObject

class UserRolDTO(
    val id:String,
    val code:String,
    val name: String,
    val permissions: UserPermissionsDTO
):DTO<UserRol>(){

    constructor(json: JSONObject) : this(
        id          = json.asString("id"),
        code        = json.asString("code"),
        name        = json.asString("name"),
        permissions = UserPermissionsDTO(json = json.asJSONArray("permissions"))
    )

    override fun toEntity(): UserRol = UserRol(
        id          = id,
        code        = code,
        name        = name,
        permissions = permissions.toEntity()
    )
}