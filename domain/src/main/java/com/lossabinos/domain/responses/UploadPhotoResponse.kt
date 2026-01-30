package com.lossabinos.domain.responses

import com.lossabinos.domain.entities.DomainEntity
import com.lossabinos.domain.entities.Photo

class UploadPhotoResponse(
    val photo: Photo
): DomainEntity()