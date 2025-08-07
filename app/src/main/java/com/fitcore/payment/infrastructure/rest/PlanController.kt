package com.fitcore.payment.presentation.rest

import com.fitcore.payment.application.PlanService
import com.fitcore.payment.presentation.dto.*
import com.fitcore.payment.presentation.mapper.PlanMapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST controller responsible for managing Plans and their Items.
 * All endpoints return messages and responses in English.
 */
@RestController
@RequestMapping("/payment/api/plans")
class PlanController(
    private val planService: PlanService,
) {

    @PostMapping
    fun createPlan(
        @RequestBody dto: PlanRequestDto,
    ): ResponseEntity<PlanResponseDto> =
        ResponseEntity.ok(
            PlanMapper.toResponse(
                planService.createPlan(PlanMapper.toDomain(dto))
            )
        )

    @GetMapping("/{planId}")
    fun getPlan(
        @PathVariable planId: String,
    ): ResponseEntity<PlanResponseDto> =
        ResponseEntity.ok(
            PlanMapper.toResponse(
                planService.getPlan(planId)
            )
        )

    @PutMapping("/{planId}")
    fun updatePlan(
        @PathVariable planId: String,
        @RequestBody dto: PlanRequestDto,
    ): ResponseEntity<PlanResponseDto> =
        ResponseEntity.ok(
            PlanMapper.toResponse(
                planService.updatePlan(planId, PlanMapper.toDomain(dto))
            )
        )

    @DeleteMapping("/{planId}")
    fun deletePlan(
        @PathVariable planId: String,
    ): ResponseEntity<Void> =
        planService.deletePlan(planId).let {
            ResponseEntity.noContent().build()
        }

    @GetMapping
    fun listPlans(): ResponseEntity<List<PlanResponseDto>> =
        ResponseEntity.ok(
            planService.listPlans().map { PlanMapper.toResponse(it) }
        )

    // -------- PLAN ITEM ROUTES ----------

    @PostMapping("/{planId}/items")
    fun addPlanItem(
        @PathVariable planId: String,
        @RequestBody dto: PlanItemRequestDto,
    ): ResponseEntity<PlanItemDto> =
        ResponseEntity.ok(
            PlanMapper.toDtoItem(
                planService.addPlanItem(planId, PlanMapper.toDomainItem(dto))
            )
        )

    @PutMapping("/{planId}/items/{itemId}")
    fun updatePlanItem(
        @PathVariable planId: String,
        @PathVariable itemId: String,
        @RequestBody dto: PlanItemUpdateDto,
    ): ResponseEntity<PlanItemDto> =
        ResponseEntity.ok(
            PlanMapper.toDtoItem(
                planService.updatePlanItem(planId, itemId, PlanMapper.toDomainItem(dto))
            )
        )

    @DeleteMapping("/{planId}/items/{itemId}")
    fun deletePlanItem(
        @PathVariable planId: String,
        @PathVariable itemId: String,
    ): ResponseEntity<Void> =
        planService.deletePlanItem(planId, itemId).let {
            ResponseEntity.noContent().build()
        }
}
