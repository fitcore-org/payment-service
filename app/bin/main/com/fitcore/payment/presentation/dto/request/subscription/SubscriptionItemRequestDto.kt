package com.fitcore.payment.presentation.dto

data class SubscriptionItemRequestDto(
    val planItemId: String? = null,
    val description: String? = null,
    val cycles: Int? = null,
    val pricingScheme: PricingSchemeDto,
    val quantity: Int? = null,
    val name: String? = null,
)
