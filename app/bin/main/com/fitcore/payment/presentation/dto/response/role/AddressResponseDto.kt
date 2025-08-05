package com.fitcore.payment.presentation.dto

data class AddressResponseDto(
    val id: String,                 // addr_XXXXXXXXXXXXXXXX
    val customerId: String?,        // cus_XXXXXXXXXXXXXXXX (opcional, pode ser nulo)
    val line1: String,
    val line2: String?,
    val zipCode: String,
    val city: String,
    val state: String,
    val country: String,
    val metadata: Map<String, Any>? = null
)
