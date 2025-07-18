package com.fitcore.payment.presentation.dto

data class SubscriptionItemResponseDto(
    val id: String,
    val planItemId: String?,
    val name: String?,
    val description: String?,
    val pricingScheme: PricingSchemeDto,
    val cycles: Int?,
    val quantity: Int?,
    val status: String?,
    val createdAt: String?,
    val updatedAt: String?,
)
