package com.fitcore.payment.presentation.controller

import com.fitcore.payment.application.RoleService
import com.fitcore.payment.presentation.dto.RoleDto
import com.fitcore.payment.presentation.mapper.toDomain
import com.fitcore.payment.presentation.mapper.toDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * REST controller responsible for managing Roles.
 * All responses and documentation are in English.
 */
@RestController
@RequestMapping("/payment/roles")
class RoleController(
    private val roleService: RoleService
) {

    @PostMapping
    fun createRole(
        @RequestBody body: RoleDto,
    ): ResponseEntity<RoleDto> =
        ResponseEntity.ok(roleService.createRole(body.toDomain()).toDto())

    @GetMapping("/{id}")
    fun getRole(
        @PathVariable id: UUID,
    ): ResponseEntity<RoleDto> =
        roleService.getRoleById(id)
            ?.let { ResponseEntity.ok(it.toDto()) }
            ?: ResponseEntity.notFound().build()

    @GetMapping
    fun getAllRoles(): ResponseEntity<List<RoleDto>> =
        ResponseEntity.ok(roleService.getAllRoles().map { it.toDto() })

    @PutMapping("/{id}")
    fun updateRole(
        @PathVariable id: UUID,
        @RequestBody body: RoleDto,
    ): ResponseEntity<RoleDto> =
        roleService.updateRole(id, body.toDomain())
            ?.let { ResponseEntity.ok(it.toDto()) }
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    fun deleteRole(
        @PathVariable id: UUID,
    ): ResponseEntity<Void> {
        roleService.deleteRole(id)
        return ResponseEntity.noContent().build()
    }
}
