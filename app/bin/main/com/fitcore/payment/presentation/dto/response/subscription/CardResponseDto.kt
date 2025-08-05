package com.fitcore.payment.presentation.dto

data class CardResponseDto(
    val id: String,
    val holderName: String,
    val brand: String?,
    val lastFourDigits: String,
    val expMonth: Int,
    val expYear: Int,
    val label: String?,
    val createdAt: String?,
)
