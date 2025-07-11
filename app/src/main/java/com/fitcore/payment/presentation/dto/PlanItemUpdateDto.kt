package com.fitcore.payment.presentation.dto

data class PlanItemUpdateDto(
    val name: String,
    val description: String? = null,
    val quantity: Int,
    val cycles: Int? = null,
    val pricingScheme: PricingSchemeDto,
    val status: String // "active" ou "inactive"
)
