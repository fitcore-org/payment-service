package com.fitcore.payment.infrastructure.persistence.entity

import jakarta.persistence.Embeddable

@Embeddable
data class AddressEmbeddable(
    var line1: String = "",
    var line2: String? = null,
    var zipCode: String = "",
    var city: String = "",
    var state: String = "",
    var country: String = "",
)
