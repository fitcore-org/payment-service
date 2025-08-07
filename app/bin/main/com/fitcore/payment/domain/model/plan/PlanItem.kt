package com.fitcore.payment.domain.model

data class PlanItem(
    val id: String? = null,
    val name: String,
    val description: String? = null,
    val quantity: Int,
    val pricingScheme: PricingScheme,
    val cycles: Int? = null,
    val status: String? = null,
)
