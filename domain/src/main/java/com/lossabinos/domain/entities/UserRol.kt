package com.lossabinos.domain.entities

import com.lossabinos.domain.valueobjects.UserPermissions

class UserRol(
    val id: String,
    val code:String,
    val name: String,
    val permissions: UserPermissions
): DomainEntity()