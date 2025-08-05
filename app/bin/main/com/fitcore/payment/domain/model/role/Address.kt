package com.fitcore.payment.domain.model

data class Address(
    val id: String? = null,                  // addr_XXXXXXXXXXXXXXXX (gerado pelo Pagar.me, opcional no cadastro)
    val customerId: String? = null,          // cus_XXXXXXXXXXXXXXXX (usado para vincular)
    val line1: String,
    val line2: String? = null,
    val zipCode: String,
    val city: String,
    val state: String,
    val country: String,
    val metadata: Map<String, Any>? = null   // campo opcional para infos extras
)
