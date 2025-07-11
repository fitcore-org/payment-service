package com.fitcore.payment.domain.model

import java.util.UUID

data class Role(
    val id: UUID,
    val name: String,
    val email: String,
    val phone: String? = null,
    val document: String? = null,
    val roleType: RoleType
)
