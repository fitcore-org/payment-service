package com.fitcore.payment.domain.model

data class Plan(
    val id: String?,
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
    val items: List<PlanItem>?,
    val metadata: Map<String, Any>?,
    val pricingScheme: PricingScheme?,
    val quantity: Int?,
    val status: String? = null
)
