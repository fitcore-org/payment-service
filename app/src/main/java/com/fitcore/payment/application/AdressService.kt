package com.fitcore.payment.application

import com.fitcore.payment.domain.model.Address
import com.fitcore.payment.domain.repository.AddressGatewayPort
import org.springframework.stereotype.Service

/**
 * Application service for managing address operations.
 * Delegates business operations to the AddressGatewayPort.
 */
@Service
class AddressService(
    private val addressGatewayPort: AddressGatewayPort,
) {
    /**
     * Creates a new address for a customer.
     * @param customerId The customer identifier.
     * @param address The address data to create.
     * @return The created Address.
     */
    fun createAddress(customerId: String, address: Address): Address =
        addressGatewayPort.createAddress(customerId, address)

    /**
     * Retrieves an address by its ID for a customer.
     * @param customerId The customer identifier.
     * @param addressId The address identifier.
     * @return The found Address.
     */
    fun getAddress(customerId: String, addressId: String): Address =
        addressGatewayPort.getAddress(customerId, addressId)

    /**
     * Updates an existing address for a customer.
     * @param customerId The customer identifier.
     * @param addressId The address identifier.
     * @param address The new address data.
     * @return The updated Address.
     */
    fun updateAddress(customerId: String, addressId: String, address: Address): Address =
        addressGatewayPort.updateAddress(customerId, addressId, address)

    /**
     * Lists all addresses for a customer.
     * @param customerId The customer identifier.
     * @return List of Address for the customer.
     */
    fun listAddresses(customerId: String): List<Address> =
        addressGatewayPort.listAddresses(customerId)

    /**
     * Deletes an address by its ID for a customer.
     * @param customerId The customer identifier.
     * @param addressId The address identifier.
     */
    fun deleteAddress(customerId: String, addressId: String) =
        addressGatewayPort.deleteAddress(customerId, addressId)
}
