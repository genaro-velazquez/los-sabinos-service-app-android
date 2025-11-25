package com.lossabinos.data.dto.responses

import com.lossabinos.data.dto.entities.UserDTO
import com.lossabinos.data.dto.utilities.asJSONObject
import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.domain.responses.LoginResponse
import org.json.JSONObject

open class LoginResponseDTO(
    json: JSONObject
) : GetBaseResponseDTO<LoginResponse>(json = json) {

    val primaryColor    = json.asJSONObject("data").asJSONObject("tenant").asJSONObject("brandingConfig").asString("primaryColor")
    val secondaryColor  = json.asJSONObject("data").asJSONObject("tenant").asJSONObject("brandingConfig").asString("secondaryColor")
    val appName         = json.asJSONObject("data").asJSONObject("tenant").asString("name")
    val accessToken     = json.asJSONObject("data").asJSONObject("tokens").asString("accessToken")
    val refreshToken    = json.asJSONObject("data").asJSONObject("tokens").asString("refreshToken")
    val client          = UserDTO(json.asJSONObject("data").asJSONObject("user"))


    override fun toEntity(): LoginResponse = LoginResponse(
        primaryColor    = primaryColor,
        secondaryColor  = secondaryColor,
        appName         = appName,
        accessToken     = accessToken,
        refreshToken    = refreshToken,
        client          = client.toEntity()
    )
}