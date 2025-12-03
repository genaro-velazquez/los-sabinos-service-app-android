package com.lossabinos.domain.valueobjects

import com.lossabinos.domain.entities.DomainEntity

class CurrentProgress(
    val itemsCompleted:Int,
    val itemTotal: Int,
    val progressPercentage: Int,
    val lastUpdated: String
): DomainEntity()