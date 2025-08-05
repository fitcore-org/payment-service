package com.fitcore.payment.domain.repository

import com.fitcore.payment.domain.model.Plan
import com.fitcore.payment.domain.model.PlanItem

/**
 * Gateway port for plan-related operations with external providers.
 */
interface PlanGatewayPort {
    /**
     * Creates a new plan.
     * @param plan The plan data to create.
     * @return The created Plan.
     */
    fun createPlan(plan: Plan): Plan

    /**
     * Retrieves a plan by its identifier.
     * @param planId The plan identifier.
     * @return The found Plan.
     */
    fun getPlan(planId: String): Plan

    /**
     * Updates a plan with new data.
     * @param planId The plan identifier.
     * @param plan The new plan data.
     * @return The updated Plan.
     */
    fun updatePlan(
        planId: String,
        plan: Plan,
    ): Plan

    /**
     * Deletes a plan by its identifier.
     * @param planId The plan identifier.
     */
    fun deletePlan(planId: String)

    /**
     * Lists all plans.
     * @return List of all Plan entities.
     */
    fun listPlans(): List<Plan>

    /**
     * Adds an item to a plan.
     * @param planId The plan identifier.
     * @param item The PlanItem to add.
     * @return The created PlanItem.
     */
    fun addPlanItem(
        planId: String,
        item: PlanItem,
    ): PlanItem

    /**
     * Updates a plan item.
     * @param planId The plan identifier.
     * @param itemId The plan item identifier.
     * @param item The new PlanItem data.
     * @return The updated PlanItem.
     */
    fun updatePlanItem(
        planId: String,
        itemId: String,
        item: PlanItem,
    ): PlanItem

    /**
     * Deletes an item from a plan.
     * @param planId The plan identifier.
     * @param itemId The plan item identifier.
     */
    fun deletePlanItem(
        planId: String,
        itemId: String,
    )
}
