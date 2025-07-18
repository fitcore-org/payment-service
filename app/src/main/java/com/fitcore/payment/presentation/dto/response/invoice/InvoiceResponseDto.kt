package com.fitcore.payment.presentation.dto

import java.time.LocalDateTime

data class InvoiceResponseDto(
    val id: String,
    val url: String?,
    val amount: Int,
    val paymentMethod: String,
    val installments: Int?,
    val status: String,
    val billingAt: LocalDateTime?,
    val seenAt: LocalDateTime?,
    val dueAt: LocalDateTime?,
    val createdAt: LocalDateTime?,
    val canceledAt: LocalDateTime?,
    val metadata: Map<String, Any?>?,
    val boletoPdfUrl: String?,
)
