package com.fitcore.payment.application

import com.fitcore.payment.domain.repository.CustomerGatewayPort
import com.fitcore.payment.domain.repository.RoleRepository
import com.fitcore.payment.presentation.dto.CustomerDto
import com.fitcore.payment.presentation.mapper.toDto
import com.fitcore.payment.presentation.mapper.toEntity
import com.fitcore.payment.presentation.mapper.toEmbeddable
import com.fitcore.payment.presentation.mapper.toDomain
import org.springframework.stereotype.Service
import java.util.*

/**
 * Application service for managing customers (roles).
 * Handles local persistence and synchronization with external services.
 */
@Service
class CustomerService(
    private val roleRepository: RoleRepository,
    private val pagarmeApi: CustomerGatewayPort,
) {
    /**
     * Creates a new customer, persists locally, and registers it with the external payment provider.
     * @param dto The CustomerDto containing customer data.
     * @return The created CustomerDto.
     */
    fun createCustomer(dto: CustomerDto): CustomerDto {
        val entity = dto.toEntity()
        val saved = roleRepository.save(entity)
        val serviceId = pagarmeApi.createCustomer(saved)
        saved.serviceId = serviceId
        roleRepository.save(saved)
        return saved.toDto()
    }

    /**
     * Retrieves a customer by its UUID.
     * @param id The customer identifier (UUID).
     * @return The CustomerDto if found, otherwise null.
     */
    fun getCustomerById(id: UUID): CustomerDto? =
        roleRepository.findById(id).map { it.toDto() }.orElse(null)

    /**
     * Lists all registered customers.
     * @return List of CustomerDto.
     */
    fun listCustomers(): List<CustomerDto> =
        roleRepository.findAll().map { it.toDto() }

    /**
     * Updates a customer both locally and (if present) in the external payment provider.
     * @param id The customer identifier (UUID).
     * @param dto The updated customer data.
     * @return The updated CustomerDto, or null if the customer does not exist.
     */
    fun updateCustomer(id: UUID, dto: CustomerDto): CustomerDto? {
        val existing = roleRepository.findById(id)
        if (existing.isPresent) {
            val entity = existing.get()
            entity.name = dto.name
            entity.email = dto.email
            entity.phone = dto.phone
            entity.document = dto.document
            entity.documentType = dto.documentType
            entity.birthdate = dto.birthdate
            entity.roleType = dto.roleType
            entity.address = dto.address?.toDomain()?.toEmbeddable()
            entity.type = dto.type
            entity.gender = dto.gender
            val saved = roleRepository.save(entity)
            if (!entity.serviceId.isNullOrBlank()) {
                pagarmeApi.updateCustomer(entity.serviceId!!, entity)
            }
            return saved.toDto()
        }
        return null
    }

    /**
     * Deletes a customer both locally and from the external payment provider.
     * @param id The customer identifier (UUID).
     */
    fun deleteCustomer(id: UUID) {
        val entity = roleRepository.findById(id)
        if (entity.isPresent) {
            val serviceId = entity.get().serviceId
            if (!serviceId.isNullOrBlank()) {
                pagarmeApi.deleteCustomer(serviceId)
            }
            roleRepository.deleteById(id)
        }
    }
}
