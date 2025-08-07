package com.fitcore.payment.presentation.mapper

import com.fitcore.payment.domain.model.RoleType
import com.fitcore.payment.infrastructure.persistence.entity.RoleEntity
import com.fitcore.payment.presentation.dto.CustomerDto
import com.fitcore.payment.presentation.dto.AddressDto
import java.util.*

/**
 * Maps CustomerDto to RoleEntity (for persistence).
 */
fun CustomerDto.toEntity(): RoleEntity =
    RoleEntity(
        id = this.id ?: UUID.randomUUID(),
        name = this.name,
        email = this.email,
        phone = this.phone,
        document = this.document,
        documentType = this.documentType,
        birthdate = this.birthdate,
        roleType = this.roleType,
        address = this.address?.toDomain()?.toEmbeddable(),
        type = this.type,
        gender = this.gender,
        serviceId = this.serviceId,
    )

/**
 * Maps RoleEntity (from database) to CustomerDto (transport layer).
 */
fun RoleEntity.toDto(): CustomerDto =
    CustomerDto(
        id = this.id,
        name = this.name,
        email = this.email,
        phone = this.phone,
        document = this.document,
        documentType = this.documentType,
        birthdate = this.birthdate,
        roleType = this.roleType,
        address = this.address?.toDomain()?.toDto(),
        type = this.type,
        gender = this.gender,
        serviceId = this.serviceId,
    )
