package com.fitcore.payment.presentation.rest

import com.fitcore.payment.application.PlanService
import com.fitcore.payment.presentation.dto.PlanRequestDto
import com.fitcore.payment.presentation.mapper.PlanMapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/plans")
class PlanController(private val planService: PlanService) {

    @PostMapping
    fun createPlan(@RequestBody dto: PlanRequestDto) =
        ResponseEntity.ok(PlanMapper.toResponse(planService.createPlan(PlanMapper.toDomain(dto))))

    @GetMapping("/{planId}")
    fun getPlan(@PathVariable planId: String) =
        ResponseEntity.ok(PlanMapper.toResponse(planService.getPlan(planId)))

    @PutMapping("/{planId}")
    fun updatePlan(@PathVariable planId: String, @RequestBody dto: PlanRequestDto) =
        ResponseEntity.ok(PlanMapper.toResponse(planService.updatePlan(planId, PlanMapper.toDomain(dto))))

    @DeleteMapping("/{planId}")
    fun deletePlan(@PathVariable planId: String) = planService.deletePlan(planId).let { ResponseEntity.noContent().build<Void>() }

    @GetMapping
    fun listPlans() =
        ResponseEntity.ok(planService.listPlans().map { PlanMapper.toResponse(it) })
}
