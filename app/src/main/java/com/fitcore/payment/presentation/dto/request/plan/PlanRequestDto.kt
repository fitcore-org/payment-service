package com.fitcore.payment.presentation.dto

data class PlanRequestDto(
    val name: String,
    val description: String?,
    val shippable: Boolean?,
    val paymentMethods: List<String>?,
    val installments: List<Int>?,
    val minimumPrice: Int?,
    val statementDescriptor: String?,
    val currency: String?,
    val interval: String?,
    val intervalCount: Int?,
    val trialPeriodDays: Int?,
    val billingType: String?,
    val billingDays: List<Int>?,
    val items: List<PlanItemDto>?,
    val metadata: Map<String, Any>?,
    val pricingScheme: PricingSchemeDto?,
    val quantity: Int?,
    val status: String?,
)
