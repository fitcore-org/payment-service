package com.fitcore.payment.domain.model

data class Address(
    val line1: String,
    val line2: String?,
    val zipCode: String,
    val city: String,
    val state: String,
    val country: String,
)
