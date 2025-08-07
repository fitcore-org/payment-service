package com.fitcore.payment.domain.repository

import com.fitcore.payment.infrastructure.persistence.entity.RoleEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

/**
 * JPA repository for RoleEntity.
 */
interface RoleRepository : JpaRepository<RoleEntity, UUID> {
    /**
     * Finds a role by the external service ID.
     * @param serviceId The external service/customer identifier.
     * @return The RoleEntity if found, otherwise null.
     */
    fun findByServiceId(serviceId: String): RoleEntity?
}
