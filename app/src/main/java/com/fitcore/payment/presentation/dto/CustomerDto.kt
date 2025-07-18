package com.fitcore.payment.presentation.dto

import com.fitcore.payment.domain.model.RoleType
import java.util.*

data class CustomerDto(
    val id: UUID?,
    val name: String,
    val email: String,
    val phone: String?,
    val document: String?,
    val documentType: String?,
    val birthdate: String?,
    val type: String?,
    val gender: String?,
    val roleType: RoleType,
    val address: AddressDto?,
    val serviceId: String? = null, // ID do Pagar.me
)
