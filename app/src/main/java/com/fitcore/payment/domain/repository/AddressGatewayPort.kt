package com.fitcore.payment.domain.repository

import com.fitcore.payment.domain.model.Address

interface AddressGatewayPort {
    fun createAddress(customerId: String, address: Address): Address
    fun getAddress(customerId: String, addressId: String): Address
    fun updateAddress(customerId: String, addressId: String, address: Address): Address
    fun listAddresses(customerId: String): List<Address>
    fun deleteAddress(customerId: String, addressId: String)
}
