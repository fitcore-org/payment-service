package com.fitcore.payment.presentation.dto

data class SubscriptionRequestDto(
    val code: String?,
    val planId: String,
    val customerId: String, // Role ID local
    val paymentMethod: String,
    val installments: Int? = 1,
    val startAt: String? = null, // yyyy-MM-dd or null for immediate
    val metadata: Map<String, Any>? = null,
    val cardId: String? = null,
    val cardToken: String? = null,
)
