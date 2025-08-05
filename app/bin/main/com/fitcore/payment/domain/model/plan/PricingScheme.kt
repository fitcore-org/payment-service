package com.fitcore.payment.domain.model

data class PricingScheme(
    val schemeType: String? = null,
    val price: Int? = null, // em centavos
)
