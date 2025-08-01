package com.fitcore.payment.presentation.controller

import com.fitcore.payment.application.CustomerService
import com.fitcore.payment.presentation.dto.CustomerDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * REST controller responsible for managing customers.
 * All endpoints and responses are in English.
 */
@RestController
@RequestMapping("/api/customers")
class CustomerController(
    private val customerService: CustomerService,
) {

    /**
     * Creates a new customer.
     * @param dto The data for the new customer.
     * @return The created customer.
     */
    @PostMapping
    fun create(
        @RequestBody dto: CustomerDto,
    ): ResponseEntity<CustomerDto> =
        ResponseEntity.ok(customerService.createCustomer(dto))

    /**
     * Retrieves a customer by their unique identifier.
     * @param id The UUID of the customer.
     * @return The customer if found, 404 otherwise.
     */
    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: UUID,
    ): ResponseEntity<CustomerDto> =
        customerService.getCustomerById(id)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    /**
     * Lists all customers.
     * @return The list of all customers.
     */
    @GetMapping
    fun list(): ResponseEntity<List<CustomerDto>> =
        ResponseEntity.ok(customerService.listCustomers())

    /**
     * Updates an existing customer by ID.
     * @param id The UUID of the customer to update.
     * @param dto The updated customer data.
     * @return The updated customer if found, 404 otherwise.
     */
    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @RequestBody dto: CustomerDto,
    ): ResponseEntity<CustomerDto> =
        customerService.updateCustomer(id, dto)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    /**
     * Deletes a customer by ID.
     * @param id The UUID of the customer to delete.
     * @return No content if deleted successfully.
     */
    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: UUID,
    ): ResponseEntity<Void> {
        customerService.deleteCustomer(id)
        return ResponseEntity.noContent().build()
    }
}
