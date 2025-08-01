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
@RequestMapping("/api/plans")
class PlanController(
    private val planService: PlanService,
) {

    /**
     * Creates a new plan.
     * @param dto Request body with plan details.
     * @return The created plan.
     */
    @PostMapping
    fun createPlan(
        @RequestBody dto: PlanRequestDto,
    ): ResponseEntity<PlanResponseDto> =
        ResponseEntity.ok(
            PlanMapper.toResponse(
                planService.createPlan(PlanMapper.toDomain(dto))
            )
        )

    /**
     * Retrieves a plan by its ID.
     * @param planId The ID of the plan.
     * @return The requested plan.
     */
    @GetMapping("/{planId}")
    fun getPlan(
        @PathVariable planId: String,
    ): ResponseEntity<PlanResponseDto> =
        ResponseEntity.ok(
            PlanMapper.toResponse(
                planService.getPlan(planId)
            )
        )

    /**
     * Updates an existing plan.
     * @param planId The ID of the plan to update.
     * @param dto The new data for the plan.
     * @return The updated plan.
     */
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

    /**
     * Deletes a plan by its ID.
     * @param planId The ID of the plan to delete.
     * @return No content if successful.
     */
    @DeleteMapping("/{planId}")
    fun deletePlan(
        @PathVariable planId: String,
    ): ResponseEntity<Void> =
        planService.deletePlan(planId).let {
            ResponseEntity.noContent().build()
        }

    /**
     * Lists all plans.
     * @return The list of all plans.
     */
    @GetMapping
    fun listPlans(): ResponseEntity<List<PlanResponseDto>> =
        ResponseEntity.ok(
            planService.listPlans().map { PlanMapper.toResponse(it) }
        )

    // -------- PLAN ITEM ROUTES ----------

    /**
     * Adds a new item to a plan.
     * @param planId The ID of the plan to add the item to.
     * @param dto The item details.
     * @return The created plan item.
     */
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

    /**
     * Updates an existing item of a plan.
     * @param planId The ID of the plan.
     * @param itemId The ID of the item to update.
     * @param dto The new data for the item.
     * @return The updated plan item.
     */
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

    /**
     * Deletes an item from a plan.
     * @param planId The ID of the plan.
     * @param itemId The ID of the item to delete.
     * @return No content if successful.
     */
    @DeleteMapping("/{planId}/items/{itemId}")
    fun deletePlanItem(
        @PathVariable planId: String,
        @PathVariable itemId: String,
    ): ResponseEntity<Void> =
        planService.deletePlanItem(planId, itemId).let {
            ResponseEntity.noContent().build()
        }
}
