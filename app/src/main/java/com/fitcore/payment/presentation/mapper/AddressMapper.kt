package com.fitcore.payment.presentation.mapper

import com.fitcore.payment.domain.model.Address
import com.fitcore.payment.infrastructure.persistence.entity.AddressEmbeddable
import com.fitcore.payment.presentation.dto.AddressDto
import com.fitcore.payment.presentation.dto.AddressRequestDto
import com.fitcore.payment.presentation.dto.AddressResponseDto

fun AddressEmbeddable.toDomain(): Address =
    Address(
        line1 = this.line1,
        line2 = this.line2,
        zipCode = this.zipCode,
        city = this.city,
        state = this.state,
        country = this.country,
    )

fun Address.toEmbeddable(): AddressEmbeddable =
    AddressEmbeddable(
        line1 = this.line1,
        line2 = this.line2,
        zipCode = this.zipCode,
        city = this.city,
        state = this.state,
        country = this.country,
    )

fun AddressDto.toDomain(): Address =
    Address(
        line1 = this.line1,
        line2 = this.line2,
        zipCode = this.zipCode,
        city = this.city,
        state = this.state,
        country = this.country,
    )

fun Address.toDto(): AddressDto =
    AddressDto(
        line1 = this.line1,
        line2 = this.line2,
        zipCode = this.zipCode,
        city = this.city,
        state = this.state,
        country = this.country,
    )

// NOVOS MAPPERS PARA REQUEST/RESPONSE DTO

fun AddressRequestDto.toDomain(): Address =
    Address(
        line1 = this.line1,
        line2 = this.line2,
        zipCode = this.zipCode,
        city = this.city,
        state = this.state,
        country = this.country,
        metadata = this.metadata
    )

fun Address.toResponseDto(id: String, customerId: String? = null): AddressResponseDto =
    AddressResponseDto(
        id = id,
        customerId = customerId,
        line1 = this.line1,
        line2 = this.line2,
        zipCode = this.zipCode,
        city = this.city,
        state = this.state,
        country = this.country,
        metadata = this.metadata
    )
