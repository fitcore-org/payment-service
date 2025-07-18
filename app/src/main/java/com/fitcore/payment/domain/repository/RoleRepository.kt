package com.fitcore.payment.domain.repository

import com.fitcore.payment.infrastructure.persistence.entity.RoleEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID
import org.springframework.data.jpa.repository.Query

interface RoleRepository : JpaRepository<RoleEntity, UUID> {
    fun findByServiceId(serviceId: String): RoleEntity?
}
