package com.fitcore.payment.presentation.dto

data class CardRequestDto(
    val number: String,
    val holderName: String,
    val expMonth: Int,
    val expYear: Int,
    val cvv: String,
    val label: String? = null,
    val holderDocument: String? = null,
    val brand: String? = null,
)
