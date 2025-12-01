package com.lossabinos.domain.valueobjects

import com.lossabinos.domain.entities.DomainEntity

class Summary(
    val totalService:Int,
    val pending:Int,
    val inProgress:Int,
    val completed: Int,
    val todayServices:Int
): DomainEntity()