package com.lossabinos.data.dto.responses

import com.lossabinos.data.dto.entities.DTO
import com.lossabinos.data.dto.utilities.asBoolean
import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.domain.responses.SignChecklistResponse
import kotlinx.serialization.json.JsonObject
import org.json.JSONObject

class SignChecklistResponseDTO(
    val success: Boolean,
    val messages:String,
    val signatureId:String,
    val serviceExecutionId:String,
    val signerId:String,
    val signatureTYpe:String,
    val signedAt: String,
    val comments: String
) : DTO<SignChecklistResponse>() {

    constructor(json: JSONObject) : this (
        success = json.asBoolean("success"),
        messages = json.asString("message"),
        signatureId = json.asString("signature_id"),
        serviceExecutionId = json.asString("service_execution_id"),
        signerId = json.asString("signer_id"),
        signatureTYpe = json.asString("signature_type"),
        signedAt = json.asString("signed_at"),
        comments = json.asString("comments")
    )

    override fun toEntity(): SignChecklistResponse = SignChecklistResponse(
        success = success,
        messages = messages,
        signatureId = signatureId,
        serviceExecutionId = serviceExecutionId,
        signerId = signerId,
        signatureTYpe = signatureTYpe,
        signedAt = signedAt,
        comments = comments
    )
}