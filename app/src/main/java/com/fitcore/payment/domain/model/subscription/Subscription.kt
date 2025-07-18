package com.fitcore.payment.domain.model

import java.time.LocalDateTime

data class Subscription(
    val id: String?,
    val code: String?,
    val planId: String,
    val customerId: String,
    val paymentMethod: String,
    val status: String? = null,
    val startAt: LocalDateTime? = null,
    val installments: Int? = 1,
    val metadata: Map<String, Any>? = null,
    val cardId: String? = null,
    val cardToken: String? = null,
)
