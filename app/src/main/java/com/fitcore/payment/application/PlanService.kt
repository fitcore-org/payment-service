package com.fitcore.payment.application

import com.fitcore.payment.domain.model.PlanItem
import com.fitcore.payment.domain.model.Plan
import com.fitcore.payment.domain.repository.PlanGatewayPort
import org.springframework.stereotype.Service

@Service
class PlanService(private val planGatewayPort: PlanGatewayPort) {
    fun createPlan(plan: Plan): Plan = planGatewayPort.createPlan(plan)
    fun getPlan(planId: String): Plan = planGatewayPort.getPlan(planId)
    fun updatePlan(planId: String, plan: Plan): Plan = planGatewayPort.updatePlan(planId, plan)
    fun deletePlan(planId: String) = planGatewayPort.deletePlan(planId)
    fun listPlans(): List<Plan> = planGatewayPort.listPlans()
    fun addPlanItem(planId: String, item: PlanItem): PlanItem = planGatewayPort.addPlanItem(planId, item)
    fun updatePlanItem(planId: String, itemId: String, item: PlanItem): PlanItem = planGatewayPort.updatePlanItem(planId, itemId, item)
    fun deletePlanItem(planId: String, itemId: String) = planGatewayPort.deletePlanItem(planId, itemId)
}