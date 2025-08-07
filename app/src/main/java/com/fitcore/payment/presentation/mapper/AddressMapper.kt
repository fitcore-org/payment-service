package com.fitcore.payment.presentation.mapper

import com.fitcore.payment.domain.model.Address
import com.fitcore.payment.infrastructure.persistence.entity.AddressEmbeddable
import com.fitcore.payment.presentation.dto.AddressDto
import com.fitcore.payment.presentation.dto.AddressRequestDto
import com.fitcore.payment.presentation.dto.AddressResponseDto

/**
 * Converts AddressEmbeddable (JPA) to Address (domain model).
 */
fun AddressEmbeddable.toDomain(): Address =
    Address(
        line1 = this.line1,
        line2 = this.line2,
        zipCode = this.zipCode,
        city = this.city,
        state = this.state,
        country = this.country,
    )

/**
 * Converts Address (domain model) to AddressEmbeddable (JPA).
 */
fun Address.toEmbeddable(): AddressEmbeddable =
    AddressEmbeddable(
        line1 = this.line1,
        line2 = this.line2,
        zipCode = this.zipCode,
        city = this.city,
        state = this.state,
        country = this.country,
    )

/**
 * Converts AddressDto (transport layer) to Address (domain model).
 */
fun AddressDto.toDomain(): Address =
    Address(
        line1 = this.line1,
        line2 = this.line2,
        zipCode = this.zipCode,
        city = this.city,
        state = this.state,
        country = this.country,
    )

/**
 * Converts Address (domain model) to AddressDto (transport layer).
 */
fun Address.toDto(): AddressDto =
    AddressDto(
        line1 = this.line1,
        line2 = this.line2,
        zipCode = this.zipCode,
        city = this.city,
        state = this.state,
        country = this.country,
    )

/**
 * Converts AddressRequestDto (API input) to Address (domain model), including optional metadata.
 */
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

/**
 * Converts Address (domain model) to AddressResponseDto (API output), supporting id and optional customerId.
 */
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
