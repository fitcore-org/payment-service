package com.fitcore.payment.presentation.controller

import com.fitcore.payment.application.RoleService
import com.fitcore.payment.domain.model.Role
import com.fitcore.payment.domain.model.RoleType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/roles")
class RoleController(private val roleService: RoleService) {

    @PostMapping
    fun createRole(@RequestBody body: Role): ResponseEntity<Role> {
        val created = roleService.createRole(
            id = body.id,
            name = body.name,
            email = body.email,
            phone = body.phone,
            document = body.document,
            roleType = body.roleType
        )
        return ResponseEntity.ok(created)
    }

    @GetMapping("/{id}")
    fun getRole(@PathVariable id: UUID): ResponseEntity<Role> {
        val role = roleService.getRoleById(id)
        return if (role != null) ResponseEntity.ok(role) else ResponseEntity.notFound().build()
    }

    @GetMapping
    fun getAllRoles(): ResponseEntity<List<Role>> {
        return ResponseEntity.ok(roleService.getAllRoles())
    }

    @PutMapping("/{id}")
    fun updateRole(@PathVariable id: UUID, @RequestBody body: Role): ResponseEntity<Role> {
        val updated = roleService.updateRole(id, body)
        return if (updated != null) ResponseEntity.ok(updated) else ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deleteRole(@PathVariable id: UUID): ResponseEntity<Void> {
        roleService.deleteRole(id)
        return ResponseEntity.noContent().build()
    }
}
