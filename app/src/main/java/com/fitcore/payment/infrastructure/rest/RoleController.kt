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
@RequestMapping("/roles")
class RoleController(
    private val roleService: RoleService
) {

    /**
     * Creates a new role.
     * @param body The role data to create.
     * @return The created role.
     */
    @PostMapping
    fun createRole(
        @RequestBody body: RoleDto,
    ): ResponseEntity<RoleDto> =
        ResponseEntity.ok(roleService.createRole(body.toDomain()).toDto())

    /**
     * Retrieves a role by its unique identifier.
     * @param id The UUID of the role.
     * @return The role if found, 404 otherwise.
     */
    @GetMapping("/{id}")
    fun getRole(
        @PathVariable id: UUID,
    ): ResponseEntity<RoleDto> =
        roleService.getRoleById(id)
            ?.let { ResponseEntity.ok(it.toDto()) }
            ?: ResponseEntity.notFound().build()

    /**
     * Retrieves all roles.
     * @return List of all roles.
     */
    @GetMapping
    fun getAllRoles(): ResponseEntity<List<RoleDto>> =
        ResponseEntity.ok(roleService.getAllRoles().map { it.toDto() })

    /**
     * Updates an existing role.
     * @param id The UUID of the role to update.
     * @param body The new data for the role.
     * @return The updated role if found, 404 otherwise.
     */
    @PutMapping("/{id}")
    fun updateRole(
        @PathVariable id: UUID,
        @RequestBody body: RoleDto,
    ): ResponseEntity<RoleDto> =
        roleService.updateRole(id, body.toDomain())
            ?.let { ResponseEntity.ok(it.toDto()) }
            ?: ResponseEntity.notFound().build()

    /**
     * Deletes a role by its unique identifier.
     * @param id The UUID of the role to delete.
     * @return No content if deleted successfully.
     */
    @DeleteMapping("/{id}")
    fun deleteRole(
        @PathVariable id: UUID,
    ): ResponseEntity<Void> {
        roleService.deleteRole(id)
        return ResponseEntity.noContent().build()
    }
}
