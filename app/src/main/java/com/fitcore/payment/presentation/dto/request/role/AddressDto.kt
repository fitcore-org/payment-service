package com.fitcore.payment.presentation.dto

data class AddressDto(
    val line1: String,
    val line2: String?,
    val zipCode: String,
    val city: String,
    val state: String,
    val country: String,
)
