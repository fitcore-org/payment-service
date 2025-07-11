package com.fitcore.payment.application

import com.fitcore.payment.domain.model.Role
import com.fitcore.payment.domain.model.RoleType
import com.fitcore.payment.infrastructure.persistence.entity.RoleEntity
import com.fitcore.payment.infrastructure.persistence.repository.RoleRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class RoleService(private val roleRepository: RoleRepository) {

    fun createRole(
        id: UUID,
        name: String,
        email: String,
        phone: String?,
        document: String?,
        roleType: RoleType
    ): Role {
        val entity = RoleEntity(
            id = id,
            name = name,
            email = email,
            phone = phone,
            document = document,
            roleType = roleType
        )
        val saved = roleRepository.save(entity)
        return saved.toDomain()
    }

    fun getRoleById(id: UUID): Role? {
        return roleRepository.findById(id).map { it.toDomain() }.orElse(null)
    }

    fun getAllRoles(): List<Role> {
        return roleRepository.findAll().map { it.toDomain() }
    }

    fun updateRole(id: UUID, updated: Role): Role? {
        val existing = roleRepository.findById(id)
        if (existing.isPresent) {
            val entity = existing.get()
            entity.name = updated.name
            entity.email = updated.email
            entity.phone = updated.phone
            entity.document = updated.document
            entity.roleType = updated.roleType
            val saved = roleRepository.save(entity)
            return saved.toDomain()
        }
        return null
    }

    fun deleteRole(id: UUID) {
        roleRepository.deleteById(id)
    }
}
