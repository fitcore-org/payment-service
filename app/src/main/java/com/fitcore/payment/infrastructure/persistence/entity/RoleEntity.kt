package com.fitcore.payment.infrastructure.persistence.entity

import jakarta.persistence.*
import java.util.UUID
import com.fitcore.payment.domain.model.RoleType

@Entity
@Table(name = "roles", uniqueConstraints = [
    UniqueConstraint(columnNames = ["email"]),
    UniqueConstraint(columnNames = ["document"])
])
class RoleEntity(

    @Id
    @Column(nullable = false, updatable = false)
    var id: UUID = UUID.randomUUID(),

    @Column(nullable = false, length = 255)
    var name: String,

    @Column(nullable = false, length = 255)
    var email: String,

    @Column(nullable = true, length = 20)
    var phone: String? = null,

    @Column(nullable = true, length = 20)
    var document: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "role_type", length = 32)
    var roleType: RoleType
) {
    fun toDomain() = com.fitcore.payment.domain.model.Role(
        id = id,
        name = name,
        email = email,
        phone = phone,
        document = document,
        roleType = roleType
    )
}
