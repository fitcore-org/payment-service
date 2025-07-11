package com.fitcore.payment.domain.repository

import com.fitcore.payment.domain.model.PlanItem
import com.fitcore.payment.domain.model.Plan

interface PlanGatewayPort {
    fun createPlan(plan: Plan): Plan
    fun getPlan(planId: String): Plan
    fun updatePlan(planId: String, plan: Plan): Plan
    fun deletePlan(planId: String)
    fun listPlans(): List<Plan>
    fun addPlanItem(planId: String, item: PlanItem): PlanItem
    fun updatePlanItem(planId: String, itemId: String, item: PlanItem): PlanItem
    fun deletePlanItem(planId: String, itemId: String)
}
