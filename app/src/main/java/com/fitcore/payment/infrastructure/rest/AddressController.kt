package com.fitcore.payment.presentation.rest

import com.fitcore.payment.application.AddressService
import com.fitcore.payment.presentation.dto.AddressRequestDto
import com.fitcore.payment.presentation.dto.AddressResponseDto
import com.fitcore.payment.presentation.mapper.toDomain
import com.fitcore.payment.presentation.mapper.toResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/customers/{customerId}/addresses")
class AddressController(
    private val addressService: AddressService,
) {

    @PostMapping
    fun createAddress(
        @PathVariable customerId: String,
        @RequestBody dto: AddressRequestDto,
    ): ResponseEntity<AddressResponseDto> {
        val address = addressService.createAddress(customerId, dto.toDomain())
        return ResponseEntity.ok(address.toResponseDto(address.id!!, customerId))
    }

    @GetMapping
    fun listAddresses(
        @PathVariable customerId: String,
    ): ResponseEntity<List<AddressResponseDto>> {
        val addresses = addressService.listAddresses(customerId)
        return ResponseEntity.ok(addresses.map { it.toResponseDto(it.id!!, customerId) })
    }

    @GetMapping("/{addressId}")
    fun getAddress(
        @PathVariable customerId: String,
        @PathVariable addressId: String,
    ): ResponseEntity<AddressResponseDto> {
        val address = addressService.getAddress(customerId, addressId)
        return ResponseEntity.ok(address.toResponseDto(address.id!!, customerId))
    }

    @PutMapping("/{addressId}")
    fun updateAddress(
        @PathVariable customerId: String,
        @PathVariable addressId: String,
        @RequestBody dto: AddressRequestDto,
    ): ResponseEntity<AddressResponseDto> {
        val updated = addressService.updateAddress(customerId, addressId, dto.toDomain())
        return ResponseEntity.ok(updated.toResponseDto(addressId, customerId))
    }

    @DeleteMapping("/{addressId}")
    fun deleteAddress(
        @PathVariable customerId: String,
        @PathVariable addressId: String,
    ): ResponseEntity<Void> {
        addressService.deleteAddress(customerId, addressId)
        return ResponseEntity.noContent().build()
    }
}
