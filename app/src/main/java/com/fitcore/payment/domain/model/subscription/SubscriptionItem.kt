package com.fitcore.payment.domain.model

data class SubscriptionItem(
    val id: String? = null,
    val planItemId: String? = null,
    val name: String? = null,
    val description: String? = null,
    val pricingScheme: PricingScheme,
    val cycles: Int? = null,
    val quantity: Int? = null,
    val status: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
)
