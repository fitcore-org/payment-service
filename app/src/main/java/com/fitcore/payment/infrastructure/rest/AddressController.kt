package com.fitcore.payment.presentation.rest

import com.fitcore.payment.application.AddressService
import com.fitcore.payment.presentation.dto.AddressRequestDto
import com.fitcore.payment.presentation.dto.AddressResponseDto
import com.fitcore.payment.presentation.mapper.toDomain
import com.fitcore.payment.presentation.mapper.toResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST controller responsible for managing customer addresses.
 * All endpoints and responses are in English.
 */
@RestController
@RequestMapping("/api/customers/{customerId}/addresses")
class AddressController(
    private val addressService: AddressService,
) {

    /**
     * Creates a new address for the given customer.
     * @param customerId The unique identifier of the customer.
     * @param dto The address data to create.
     * @return The created address.
     */
    @PostMapping
    fun createAddress(
        @PathVariable customerId: String,
        @RequestBody dto: AddressRequestDto,
    ): ResponseEntity<AddressResponseDto> {
        val address = addressService.createAddress(customerId, dto.toDomain())
        return ResponseEntity.ok(address.toResponseDto(address.id!!, customerId))
    }

    /**
     * Lists all addresses for the given customer.
     * @param customerId The unique identifier of the customer.
     * @return The list of addresses.
     */
    @GetMapping
    fun listAddresses(
        @PathVariable customerId: String,
    ): ResponseEntity<List<AddressResponseDto>> {
        val addresses = addressService.listAddresses(customerId)
        return ResponseEntity.ok(addresses.map { it.toResponseDto(it.id!!, customerId) })
    }

    /**
     * Retrieves an address by its ID for a given customer.
     * @param customerId The unique identifier of the customer.
     * @param addressId The unique identifier of the address.
     * @return The address details.
     */
    @GetMapping("/{addressId}")
    fun getAddress(
        @PathVariable customerId: String,
        @PathVariable addressId: String,
    ): ResponseEntity<AddressResponseDto> {
        val address = addressService.getAddress(customerId, addressId)
        return ResponseEntity.ok(address.toResponseDto(address.id!!, customerId))
    }

    /**
     * Updates an address for a given customer.
     * @param customerId The unique identifier of the customer.
     * @param addressId The unique identifier of the address.
     * @param dto The updated address data.
     * @return The updated address details.
     */
    @PutMapping("/{addressId}")
    fun updateAddress(
        @PathVariable customerId: String,
        @PathVariable addressId: String,
        @RequestBody dto: AddressRequestDto,
    ): ResponseEntity<AddressResponseDto> {
        val updated = addressService.updateAddress(customerId, addressId, dto.toDomain())
        return ResponseEntity.ok(updated.toResponseDto(addressId, customerId))
    }

    /**
     * Deletes an address for a given customer.
     * @param customerId The unique identifier of the customer.
     * @param addressId The unique identifier of the address to delete.
     * @return No content if successfully deleted.
     */
    @DeleteMapping("/{addressId}")
    fun deleteAddress(
        @PathVariable customerId: String,
        @PathVariable addressId: String,
    ): ResponseEntity<Void> {
        addressService.deleteAddress(customerId, addressId)
        return ResponseEntity.noContent().build()
    }
}
