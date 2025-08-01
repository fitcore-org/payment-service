package com.fitcore.payment.presentation.mapper

import com.fitcore.payment.domain.model.Role
import com.fitcore.payment.infrastructure.persistence.entity.RoleEntity
import com.fitcore.payment.presentation.dto.RoleDto
import java.util.*

/**
 * Maps RoleEntity (persistence) to Role (domain model).
 */
fun RoleEntity.toDomain(): Role =
    Role(
        id = this.id,
        name = this.name,
        email = this.email,
        phone = this.phone,
        document = this.document,
        documentType = this.documentType,
        type = this.type,
        birthdate = this.birthdate,
        gender = this.gender,
        roleType = this.roleType,
        address = this.address?.toDomain(),
        serviceId = this.serviceId,
    )

/**
 * Maps Role (domain model) to RoleEntity (for persistence).
 */
fun Role.toEntity(): RoleEntity =
    RoleEntity(
        id = this.id,
        name = this.name,
        email = this.email,
        phone = this.phone,
        document = this.document,
        documentType = this.documentType,
        type = this.type,
        birthdate = this.birthdate,
        gender = this.gender,
        roleType = this.roleType,
        address = this.address?.toEmbeddable(),
        serviceId = this.serviceId,
    )

/**
 * Maps RoleDto (API/transport layer) to Role (domain model).
 */
fun RoleDto.toDomain(): Role =
    Role(
        id = this.id ?: UUID.randomUUID(),
        name = this.name,
        email = this.email,
        phone = this.phone,
        document = this.document,
        documentType = this.documentType,
        type = this.type,
        birthdate = this.birthdate,
        gender = this.gender,
        roleType = this.roleType,
        address = this.address?.toDomain(),
        serviceId = this.serviceId,
    )

/**
 * Maps Role (domain model) to RoleDto (API/transport layer).
 */
fun Role.toDto(): RoleDto =
    RoleDto(
        id = this.id,
        name = this.name,
        email = this.email,
        phone = this.phone,
        document = this.document,
        documentType = this.documentType,
        type = this.type,
        birthdate = this.birthdate,
        gender = this.gender,
        roleType = this.roleType,
        address = this.address?.toDto(),
        serviceId = this.serviceId,
    )
