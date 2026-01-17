package com.lossabinos.data.dto.valueobjects

import com.lossabinos.data.dto.entities.DTO
import com.lossabinos.data.dto.utilities.asInt
import com.lossabinos.data.dto.utilities.asJSONObject
import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.domain.valueobjects.Token
import org.json.JSONObject

class TokenDTO(
    val accessToken:String,
    val refreshToken: String,
    val tokenType:String,
    val expiresIn: Int
) : DTO<Token>() {

    constructor(json: JSONObject) : this (
        accessToken = json.asJSONObject("data").asJSONObject("tokens").asString("accessToken"),
        refreshToken = json.asJSONObject("data").asJSONObject("tokens").asString("refreshToken"),
        tokenType = json.asJSONObject("data").asJSONObject("tokens").asString("tokenType"),
        expiresIn = json.asJSONObject("data").asJSONObject("tokens").asInt("expiresIn")
    )

    override fun toEntity(): Token  = Token(
        accessToken = accessToken,
        refreshToken = refreshToken,
        tokenType = tokenType,
        expiresIn = expiresIn
    )
}