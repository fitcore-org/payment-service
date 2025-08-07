package com.fitcore.payment.presentation.dto

data class AddressRequestDto(
    val line1: String,
    val line2: String? = null,
    val zipCode: String,
    val city: String,
    val state: String,
    val country: String,
    val metadata: Map<String, Any>? = null
)
