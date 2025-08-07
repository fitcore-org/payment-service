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
 * Service responsible for creating, updating and deleting roles.  It
 * orchestrates persisting role entities locally as well as propagating
 * changes to Pagar.me.  This implementation has been adjusted so that
 * updates will always synchronise with Pagar.me: if a role does not yet
 * have a serviceId associated (i.e. Pagar.me customer ID), a new
 * customer will be created upon update and the returned identifier
 * stored.  This prevents situations where calling PUT on /roles fails to
 * update the external customer or where POST requests inadvertently
 * update a record without synchronising with Pagar.me.
 */
@Service
class RoleService(
    private val roleRepository: RoleRepository,
    private val pagarmeApi: CustomerGatewayPort,
) {
    fun createRole(role: Role): Role {
        val entity = role.toEntity()
        val saved = roleRepository.save(entity)
        val serviceId = pagarmeApi.createCustomer(saved)
        saved.serviceId = serviceId
        roleRepository.save(saved)
        return saved.toDomain()
    }

    fun getRoleById(id: UUID): Role? = roleRepository.findById(id).map { it.toDomain() }.orElse(null)

    fun getAllRoles(): List<Role> = roleRepository.findAll().map { it.toDomain() }

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

            val currentServiceId = entity.serviceId
            if (currentServiceId.isNullOrBlank()) {
                val newServiceId = pagarmeApi.createCustomer(entity)
                entity.serviceId = newServiceId
                roleRepository.save(entity)
            } else {
                pagarmeApi.updateCustomer(currentServiceId, entity)
            }
            return entity.toDomain()
        }
        return null
    }

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