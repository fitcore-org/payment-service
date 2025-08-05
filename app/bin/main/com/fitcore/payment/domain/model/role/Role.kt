package com.fitcore.payment.domain.model

import java.util.UUID

data class Role(
    val id: UUID,
    val name: String,
    val email: String,
    val phone: String?,
    val document: String?, // CPF/CNPJ
    val documentType: String?, // "cpf" ou "cnpj"
    val type: String?, // "individual" ou "corporation" → obrigatório pro Pagar.me
    val birthdate: String?, // "YYYY-MM-DD"
    val gender: String?, // "male", "female" ou "other" → opcional
    val roleType: RoleType,
    val address: Address?,
    val serviceId: String?,
)
