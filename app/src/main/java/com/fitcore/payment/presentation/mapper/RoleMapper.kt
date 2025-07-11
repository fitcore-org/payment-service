package com.fitcore.payment.presentation.mapper

import com.fitcore.payment.domain.model.Role
import com.fitcore.payment.infrastructure.persistence.entity.RoleEntity

fun Role.toEntity(): RoleEntity = RoleEntity(
    id = this.id,
    name = this.name,
    email = this.email,
    phone = this.phone,
    document = this.document,
    roleType = this.roleType
)
