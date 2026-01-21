package com.lossabinos.domain.responses

import com.lossabinos.domain.entities.DomainEntity

class SignChecklistResponse(
    val success: Boolean,
    val messages:String,
    val signatureId:String,
    val serviceExecutionId:String,
    val signerId:String,
    val signatureTYpe:String,
    val signedAt: String,
    val comments: String
): DomainEntity()