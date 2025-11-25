package com.lossabinos.data.dto.entities

import com.lossabinos.data.dto.utilities.asBoolean
import com.lossabinos.data.dto.utilities.asJSONObject
import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.domain.entities.User
import org.json.JSONObject

class UserDTO(
    json: JSONObject
) : DTO<User>() {

    val id          = json.asString("id")
    val firstName   = json.asString("firstName")
    val lastName    = json.asString("lastName")
    val email       = json.asString("email")
    val isAdmin     = json.asBoolean("isAdmin")
    val userRol     = UserRolDTO(json = json.asJSONObject("role"))

    override fun toEntity(): User = User(
        id          = id,
        firstName   = firstName,
        lastName    = lastName,
        email       = email,
        isAdmin     = isAdmin,
        userRol     = userRol.toEntity()
    )
}
