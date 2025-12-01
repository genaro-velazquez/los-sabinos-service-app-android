package com.lossabinos.domain.valueobjects

import com.lossabinos.domain.entities.DomainEntity

class Pagination(
    val page:Int,
    val limit:Int,
    val totalPages:Int,
    val totalResults:Int
): DomainEntity()