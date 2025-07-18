package com.fitcore.payment.domain.repository

import com.fitcore.payment.infrastructure.persistence.entity.RoleEntity

interface CustomerGatewayPort {
    fun createCustomer(role: RoleEntity): String

    fun updateCustomer(
        serviceId: String,
        role: RoleEntity,
    )

    fun deleteCustomer(serviceId: String)
}
