package com.fitcore.payment.domain.repository

import com.fitcore.payment.domain.model.Address

/**
 * Gateway port for address-related operations with external providers.
 */
interface AddressGatewayPort {
    /**
     * Creates a new address for a customer.
     * @param customerId The customer identifier.
     * @param address The address data to create.
     * @return The created Address.
     */
    fun createAddress(customerId: String, address: Address): Address

    /**
     * Retrieves an address by its ID for a customer.
     * @param customerId The customer identifier.
     * @param addressId The address identifier.
     * @return The found Address.
     */
    fun getAddress(customerId: String, addressId: String): Address

    /**
     * Updates an existing address for a customer.
     * @param customerId The customer identifier.
     * @param addressId The address identifier.
     * @param address The new address data.
     * @return The updated Address.
     */
    fun updateAddress(customerId: String, addressId: String, address: Address): Address

    /**
     * Lists all addresses for a customer.
     * @param customerId The customer identifier.
     * @return List of addresses for the customer.
     */
    fun listAddresses(customerId: String): List<Address>

    /**
     * Deletes an address by its ID for a customer.
     * @param customerId The customer identifier.
     * @param addressId The address identifier.
     */
    fun deleteAddress(customerId: String, addressId: String)
}
