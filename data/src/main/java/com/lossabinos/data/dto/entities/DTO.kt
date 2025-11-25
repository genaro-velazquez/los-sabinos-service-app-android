package com.lossabinos.data.dto.entities

import com.lossabinos.domain.entities.DomainEntity

abstract class DTO<E> where E : DomainEntity {
    abstract fun toEntity():E
}