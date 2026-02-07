package com.lossabinos.domain.valueobjects

data class UploadedPhoto (
    val localPhotoId: String,
    val remotePhotoId: String,
    val remoteUrl: String,
    val uploadedAt: String
)