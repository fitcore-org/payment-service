package com.fitcore.payment.application

import com.fitcore.payment.domain.repository.CustomerGatewayPort
import com.fitcore.payment.domain.model.Role
import com.fitcore.payment.domain.repository.RoleRepository
import com.fitcore.payment.presentation.mapper.toDomain
import com.fitcore.payment.presentation.mapper.toEmbeddable
import com.fitcore.payment.presentation.mapper.toEntity
import org.springframework.stereotype.Service
import java.util.*

/**
 * Application service for managing roles (users, admins, customers, etc).
 * Handles local persistence and synchronization with external payment providers.
 */
@Service
class RoleService(
    private val roleRepository: RoleRepository,
    private val pagarmeApi: CustomerGatewayPort,
) {
    /**
     * Creates a new role, saves it locally and on the external provider.
     * @param role The role data to create.
     * @return The created Role.
     */
    fun createRole(role: Role): Role {
        val entity = role.toEntity()
        val saved = roleRepository.save(entity)
        val serviceId = pagarmeApi.createCustomer(saved)
        saved.serviceId = serviceId
        roleRepository.save(saved)
        return saved.toDomain()
    }

    /**
     * Retrieves a role by its UUID.
     * @param id The role identifier (UUID).
     * @return The Role if found, otherwise null.
     */
    fun getRoleById(id: UUID): Role? =
        roleRepository.findById(id).map { it.toDomain() }.orElse(null)

    /**
     * Lists all registered roles.
     * @return List of Role entities.
     */
    fun getAllRoles(): List<Role> =
        roleRepository.findAll().map { it.toDomain() }

    /**
     * Updates a role both locally and in the external provider.
     * @param id The role identifier (UUID).
     * @param updated The updated role data.
     * @return The updated Role, or null if not found.
     */
    fun updateRole(id: UUID, updated: Role): Role? {
        val existing = roleRepository.findById(id)
        if (existing.isPresent) {
            val entity = existing.get()
            entity.name = updated.name
            entity.email = updated.email
            entity.phone = updated.phone
            entity.document = updated.document
            entity.documentType = updated.documentType
            entity.birthdate = updated.birthdate
            entity.roleType = updated.roleType
            entity.address = updated.address?.toEmbeddable()
            entity.type = updated.type
            entity.gender = updated.gender
            roleRepository.save(entity)
            if (!entity.serviceId.isNullOrBlank()) {
                pagarmeApi.updateCustomer(entity.serviceId!!, entity)
            }
            return entity.toDomain()
        }
        return null
    }

    /**
     * Deletes a role both locally and from the external provider.
     * @param id The role identifier (UUID).
     */
    fun deleteRole(id: UUID) {
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
