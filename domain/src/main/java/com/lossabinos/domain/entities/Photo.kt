package com.lossabinos.domain.entities

class Photo(
    val id:String,
    val url: String,
    val description: String,
    val uploadedAt: String
): DomainEntity()