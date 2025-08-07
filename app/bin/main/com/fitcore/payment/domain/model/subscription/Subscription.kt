package com.fitcore.payment.domain.model

import java.time.LocalDateTime

/**
 * Domain model representing a subscription within the payment service.  This
 * model mirrors the important fields returned by Pagar.me for a
 * subscription and is used across the service layer.  In addition to the
 * basic identifiers such as the subscription and plan IDs, the model now
 * captures the name and value of the associated plan.  These two new
 * properties (planName and planValue) are populated when reading a
 * subscription back from Pagar.me and make it easier for clients to show
 * human‑friendly plan information without making extra API calls.
 */
data class Subscription(
    val id: String?,
    val code: String?,
    val planId: String,
    val planName: String? = null,
    val planValue: Int? = null,
    val customerId: String,
    val paymentMethod: String,
    val status: String? = null,
    val startAt: LocalDateTime? = null,
    val installments: Int? = 1,
    val metadata: Map<String, Any>? = null,
    val cardId: String? = null,
    val cardToken: String? = null,
)