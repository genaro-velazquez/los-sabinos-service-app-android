package com.lossabinos.domain.valueobjects

import com.lossabinos.domain.entities.DomainEntity

class UserPermissions(
    val permissions: Array<String>
): DomainEntity()
