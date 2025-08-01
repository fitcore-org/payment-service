package com.fitcore.payment.domain.repository

import com.fitcore.payment.infrastructure.persistence.entity.RoleEntity

/**
 * Gateway port for customer-related operations with external providers.
 */
interface CustomerGatewayPort {
    /**
     * Creates a new customer using the given role data.
     * @param role The role entity containing customer data.
     * @return The external service ID of the created customer.
     */
    fun createCustomer(role: RoleEntity): String

    /**
     * Updates a customer using the given service/customer ID and role data.
     * @param serviceId The external customer identifier.
     * @param role The updated role entity data.
     */
    fun updateCustomer(
        serviceId: String,
        role: RoleEntity,
    )

    /**
     * Deletes a customer by its external ID.
     * @param serviceId The external customer identifier.
     */
    fun deleteCustomer(serviceId: String)
}
