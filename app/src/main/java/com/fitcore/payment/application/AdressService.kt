package com.fitcore.payment.application

import com.fitcore.payment.domain.model.Address
import com.fitcore.payment.domain.repository.AddressGatewayPort
import org.springframework.stereotype.Service

@Service
class AddressService(
    private val addressGatewayPort: AddressGatewayPort,
) {
    fun createAddress(customerId: String, address: Address): Address =
        addressGatewayPort.createAddress(customerId, address)

    fun getAddress(customerId: String, addressId: String): Address =
        addressGatewayPort.getAddress(customerId, addressId)

    fun updateAddress(customerId: String, addressId: String, address: Address): Address =
        addressGatewayPort.updateAddress(customerId, addressId, address)

    fun listAddresses(customerId: String): List<Address> =
        addressGatewayPort.listAddresses(customerId)

    fun deleteAddress(customerId: String, addressId: String) =
        addressGatewayPort.deleteAddress(customerId, addressId)
}
