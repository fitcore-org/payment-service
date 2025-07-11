package com.fitcore.payment.presentation.dto

data class PlanItemRequestDto(
    val name: String,
    val description: String? = null,
    val quantity: Int,
    val cycles: Int? = null,
    val pricingScheme: PricingSchemeDto
)
