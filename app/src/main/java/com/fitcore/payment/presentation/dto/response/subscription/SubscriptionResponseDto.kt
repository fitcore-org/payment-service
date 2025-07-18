package com.fitcore.payment.presentation.dto

import java.time.LocalDateTime

data class SubscriptionResponseDto(
    val id: String,
    val code: String?,
    val planId: String,
    val customerId: String,
    val paymentMethod: String,
    val status: String?,
    val startAt: LocalDateTime?,
    val installments: Int?,
    val metadata: Map<String, Any>?,
    val cardToken: String? = null,
)
