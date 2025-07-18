package com.fitcore.payment.infrastructure.persistence.entity

import com.fitcore.payment.domain.model.RoleType
import jakarta.persistence.*
import java.util.*

@Entity
@Table(
    name = "roles",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["email"]),
        UniqueConstraint(columnNames = ["document"]),
    ],
)
class RoleEntity(
    @Id
    @Column(nullable = false, updatable = false)
    var id: UUID = UUID.randomUUID(),
    @Column(nullable = false, length = 255)
    var name: String,
    @Column(nullable = false, length = 255)
    var email: String,
    @Column(nullable = true, length = 32)
    var type: String? = null, // "individual" ou "corporation"
    @Column(nullable = true, length = 16)
    var gender: String? = null, // "male", "female" ou "other"
    @Column(nullable = true, length = 20)
    var phone: String? = null,
    @Column(nullable = true, length = 32)
    var document: String? = null, // CPF/CNPJ
    @Column(nullable = true, length = 10)
    var documentType: String? = null, // "cpf" ou "cnpj"
    @Column(nullable = true, length = 12)
    var birthdate: String? = null, // "YYYY-MM-DD"
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "role_type", length = 32)
    var roleType: RoleType,
    @Embedded
    var address: AddressEmbeddable? = null,
    @Column(name = "service_id", nullable = true, length = 64)
    var serviceId: String? = null, // ID externo do Pagar.me
) {
    constructor() : this(
        id = UUID.randomUUID(),
        name = "",
        email = "",
        phone = null,
        document = null,
        documentType = null,
        birthdate = null,
        roleType = RoleType.STUDENT,
        address = null,
        type = null,
        gender = null,
        serviceId = null,
    )
}
