package com.fitcore.payment.infrastructure.persistence.entity

import com.fitcore.payment.domain.model.RoleType
import jakarta.persistence.*
import java.util.*

/**
 * JPA entity representing a role (user, admin, customer, etc).
 * Holds core profile data, contact, address, and external service linkage.
 */
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

    /**
     * Role type: "individual" or "corporation"
     */
    @Column(nullable = true, length = 32)
    var type: String? = null,

    /**
     * Gender: "male", "female" or "other"
     */
    @Column(nullable = true, length = 16)
    var gender: String? = null,

    /**
     * Main phone number. Format: [DD][number], e.g., 81999998888
     */
    @Column(nullable = true, length = 20)
    var phone: String? = null,

    /**
     * National document, e.g., CPF/CNPJ
     */
    @Column(nullable = true, length = 32)
    var document: String? = null,

    /**
     * Document type: "cpf" or "cnpj"
     */
    @Column(nullable = true, length = 10)
    var documentType: String? = null,

    /**
     * Date of birth in ISO format (YYYY-MM-DD)
     */
    @Column(nullable = true, length = 12)
    var birthdate: String? = null,

    /**
     * Application-specific role type (e.g., STUDENT, ADMIN, OWNER)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "role_type", length = 32)
    var roleType: RoleType,

    /**
     * Embedded address information (nullable).
     */
    @Embedded
    var address: AddressEmbeddable? = null,

    /**
     * External service ID (e.g., Pagar.me's customer ID).
     */
    @Column(name = "service_id", nullable = true, length = 64)
    var serviceId: String? = null,
) {
    /**
     * Default no-args constructor for JPA/Hibernate.
     */
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
