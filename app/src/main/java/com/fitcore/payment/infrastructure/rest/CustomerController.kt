package com.fitcore.payment.presentation.controller

import com.fitcore.payment.application.CustomerService
import com.fitcore.payment.presentation.dto.CustomerDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/customers")
class CustomerController(
    private val customerService: CustomerService,
) {
    @PostMapping
    fun create(
        @RequestBody dto: CustomerDto,
    ) = ResponseEntity.ok(customerService.createCustomer(dto))

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: UUID,
    ) = customerService.getCustomerById(id)?.let { ResponseEntity.ok(it) }
        ?: ResponseEntity.notFound().build()

    @GetMapping
    fun list() = ResponseEntity.ok(customerService.listCustomers())

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @RequestBody dto: CustomerDto,
    ) = customerService.updateCustomer(id, dto)?.let { ResponseEntity.ok(it) }
        ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: UUID,
    ): ResponseEntity<Void> {
        customerService.deleteCustomer(id)
        return ResponseEntity.noContent().build()
    }
}
