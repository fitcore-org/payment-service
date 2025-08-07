package com.fitcore.payment.application

import com.fitcore.payment.domain.model.Plan
import com.fitcore.payment.domain.model.PlanItem
import com.fitcore.payment.domain.repository.PlanGatewayPort
import org.springframework.stereotype.Service

/**
 * Application service for managing plans and their items.
 * Delegates business operations to the PlanGatewayPort.
 */
@Service
class PlanService(private val planGatewayPort: PlanGatewayPort) {
    /**
     * Creates a new plan.
     * @param plan The plan data to create.
     * @return The created Plan.
     */
    fun createPlan(plan: Plan): Plan = planGatewayPort.createPlan(plan)

    /**
     * Retrieves a plan by its identifier.
     * @param planId The plan identifier.
     * @return The found Plan.
     */
    fun getPlan(planId: String): Plan = planGatewayPort.getPlan(planId)

    /**
     * Updates a plan with new data.
     * @param planId The plan identifier.
     * @param plan The updated plan data.
     * @return The updated Plan.
     */
    fun updatePlan(
        planId: String,
        plan: Plan,
    ): Plan = planGatewayPort.updatePlan(planId, plan)

    /**
     * Deletes a plan by its identifier.
     * @param planId The plan identifier.
     */
    fun deletePlan(planId: String) = planGatewayPort.deletePlan(planId)

    /**
     * Lists all plans.
     * @return List of all plans.
     */
    fun listPlans(): List<Plan> = planGatewayPort.listPlans()

    /**
     * Adds an item to a plan.
     * @param planId The plan identifier.
     * @param item The item to add.
     * @return The created PlanItem.
     */
    fun addPlanItem(
        planId: String,
        item: PlanItem,
    ): PlanItem = planGatewayPort.addPlanItem(planId, item)

    /**
     * Updates a plan item.
     * @param planId The plan identifier.
     * @param itemId The plan item identifier.
     * @param item The updated PlanItem data.
     * @return The updated PlanItem.
     */
    fun updatePlanItem(
        planId: String,
        itemId: String,
        item: PlanItem,
    ): PlanItem = planGatewayPort.updatePlanItem(planId, itemId, item)

    /**
     * Deletes an item from a plan.
     * @param planId The plan identifier.
     * @param itemId The plan item identifier.
     */
    fun deletePlanItem(
        planId: String,
        itemId: String,
    ) = planGatewayPort.deletePlanItem(planId, itemId)
}
