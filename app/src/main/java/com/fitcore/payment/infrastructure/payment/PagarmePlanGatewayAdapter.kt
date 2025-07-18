package com.fitcore.payment.infrastructure.payment

import com.fitcore.payment.domain.model.Plan
import com.fitcore.payment.domain.model.PlanItem
import com.fitcore.payment.domain.model.PricingScheme
import com.fitcore.payment.domain.repository.PlanGatewayPort
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class PagarmePlanGatewayAdapter(
    @Value("\${pagarme.api-key}") private val apiKey: String,
    @Value("\${pagarme.base-url}") private val baseUrl: String,
) : PlanGatewayPort {
    private val restTemplate = RestTemplate()

    private fun buildHeaders(): HttpHeaders {
        val basicAuth = java.util.Base64.getEncoder().encodeToString("$apiKey:".toByteArray())
        return HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            set("Authorization", "Basic $basicAuth")
            accept = listOf(MediaType.APPLICATION_JSON)
        }
    }

    private fun parsePlanFromResponse(resp: Map<String, Any?>): Plan =
        Plan(
            id = resp["id"]?.toString(),
            name = resp["name"]?.toString() ?: "",
            description = resp["description"] as? String,
            shippable = resp["shippable"] as? Boolean,
            paymentMethods = (resp["payment_methods"] as? List<*>)?.filterIsInstance<String>(),
            installments = (resp["installments"] as? List<*>)?.mapNotNull { (it as? Number)?.toInt() },
            minimumPrice = (resp["minimum_price"] as? Number)?.toInt(),
            statementDescriptor = resp["statement_descriptor"] as? String,
            currency = resp["currency"] as? String,
            interval = resp["interval"] as? String,
            intervalCount = (resp["interval_count"] as? Number)?.toInt(),
            trialPeriodDays = (resp["trial_period_days"] as? Number)?.toInt(),
            billingType = resp["billing_type"] as? String,
            billingDays = (resp["billing_days"] as? List<*>)?.mapNotNull { (it as? Number)?.toInt() },
            items =
                (resp["items"] as? List<*>)?.mapNotNull { item ->
                    if (item is Map<*, *>) {
                        PlanItem(
                            name = item["name"]?.toString() ?: "",
                            quantity = (item["quantity"] as? Number)?.toInt() ?: 1,
                            pricingScheme =
                                (item["pricing_scheme"] as? Map<*, *>)?.let { scheme ->
                                    PricingScheme(
                                        schemeType = scheme["scheme_type"]?.toString(),
                                        price = (scheme["price"] as? Number)?.toInt(),
                                    )
                                } ?: PricingScheme(),
                            cycles = (item["cycles"] as? Number)?.toInt(),
                        )
                    } else {
                        null
                    }
                },
            metadata = resp["metadata"] as? Map<String, Any>,
            pricingScheme =
                (resp["pricing_scheme"] as? Map<*, *>)?.let { scheme ->
                    PricingScheme(
                        schemeType = scheme["scheme_type"]?.toString(),
                        price = (scheme["price"] as? Number)?.toInt(),
                    )
                },
            quantity = (resp["quantity"] as? Number)?.toInt(),
            status = resp["status"]?.toString(),
        )

    override fun createPlan(plan: Plan): Plan {
        val url = "$baseUrl/plans"
        val headers = buildHeaders()
        val body =
            mutableMapOf<String, Any?>(
                "name" to plan.name,
                "description" to plan.description,
                "shippable" to plan.shippable,
                "payment_methods" to plan.paymentMethods,
                "installments" to plan.installments,
                "minimum_price" to plan.minimumPrice,
                "statement_descriptor" to plan.statementDescriptor,
                "currency" to plan.currency,
                "interval" to plan.interval,
                "interval_count" to plan.intervalCount,
                "trial_period_days" to plan.trialPeriodDays,
                "billing_type" to plan.billingType,
                "billing_days" to if (plan.billingType == "exact_day") plan.billingDays else null,
                "items" to
                    plan.items?.map {
                        mutableMapOf<String, Any?>(
                            "name" to it.name,
                            "quantity" to it.quantity,
                            "pricing_scheme" to
                                mapOf(
                                    "scheme_type" to it.pricingScheme.schemeType,
                                    "price" to it.pricingScheme.price,
                                ),
                            "cycles" to it.cycles,
                        ).filterValues { v -> v != null }
                    },
                "metadata" to plan.metadata,
                "pricing_scheme" to
                    plan.pricingScheme?.let {
                        mapOf(
                            "scheme_type" to it.schemeType,
                            "price" to it.price,
                        ).filterValues { v -> v != null }
                    },
                "quantity" to plan.quantity,
            ).filterValues { it != null }

        val response = restTemplate.postForEntity(url, HttpEntity(body, headers), Map::class.java)
        return parsePlanFromResponse(response.body as Map<String, Any?>)
    }

    override fun getPlan(planId: String): Plan {
        val url = "$baseUrl/plans/$planId"
        val headers = buildHeaders()
        val response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity(null, headers), Map::class.java)
        return parsePlanFromResponse(response.body as Map<String, Any?>)
    }

    override fun updatePlan(
        planId: String,
        plan: Plan,
    ): Plan {
        val url = "$baseUrl/plans/$planId"
        val headers = buildHeaders()
        val body =
            mutableMapOf<String, Any?>(
                "name" to plan.name,
                "description" to plan.description,
                "status" to plan.status,
                "payment_methods" to plan.paymentMethods,
                "installments" to plan.installments,
                "minimum_price" to plan.minimumPrice,
                "statement_descriptor" to plan.statementDescriptor,
                "currency" to plan.currency,
                "interval" to plan.interval,
                "interval_count" to plan.intervalCount,
                "billing_type" to plan.billingType,
                "billing_days" to if (plan.billingType == "exact_day") plan.billingDays else null,
                "items" to
                    plan.items?.map {
                        mutableMapOf<String, Any?>(
                            "name" to it.name,
                            "quantity" to it.quantity,
                            "pricing_scheme" to
                                mapOf(
                                    "scheme_type" to it.pricingScheme.schemeType,
                                    "price" to it.pricingScheme.price,
                                ),
                            "cycles" to it.cycles,
                        ).filterValues { v -> v != null }
                    },
                "metadata" to plan.metadata,
                "pricing_scheme" to
                    plan.pricingScheme?.let {
                        mapOf(
                            "scheme_type" to it.schemeType,
                            "price" to it.price,
                        ).filterValues { v -> v != null }
                    },
                "quantity" to plan.quantity,
            ).filterValues { it != null }

        val response = restTemplate.exchange(url, HttpMethod.PUT, HttpEntity(body, headers), Map::class.java)
        return parsePlanFromResponse(response.body as Map<String, Any?>)
    }

    override fun deletePlan(planId: String) {
        val url = "$baseUrl/plans/$planId"
        val headers = buildHeaders()
        restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity(null, headers), Void::class.java)
    }

    override fun listPlans(): List<Plan> {
        val url = "$baseUrl/plans"
        val headers = buildHeaders()
        val response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity(null, headers), Map::class.java)
        val body = response.body as Map<String, Any>
        val data = body["data"] as? List<Map<String, Any?>> ?: emptyList()
        return data.map { parsePlanFromResponse(it) }
    }

    override fun addPlanItem(
        planId: String,
        item: PlanItem,
    ): PlanItem {
        val url = "$baseUrl/plans/$planId/items"
        val headers = buildHeaders()
        val body =
            mutableMapOf(
                "name" to item.name,
                "description" to item.description,
                "quantity" to item.quantity,
                "cycles" to item.cycles,
                "pricing_scheme" to
                    mapOf(
                        "price" to item.pricingScheme.price,
                        "scheme_type" to item.pricingScheme.schemeType,
                    ),
            ).filterValues { it != null }

        val response = restTemplate.postForEntity(url, HttpEntity(body, headers), Map::class.java)
        val respBody = response.body as Map<String, Any?>
        return PlanItem(
            id = respBody["id"]?.toString(),
            name = respBody["name"]?.toString() ?: "",
            description = respBody["description"] as? String,
            quantity = (respBody["quantity"] as? Number)?.toInt() ?: 1,
            cycles = (respBody["cycles"] as? Number)?.toInt(),
            pricingScheme =
                PricingScheme(
                    schemeType = (respBody["pricing_scheme"] as? Map<*, *>)?.get("scheme_type")?.toString(),
                    price = (respBody["pricing_scheme"] as? Map<*, *>)?.get("price")?.let { (it as? Number)?.toInt() },
                ),
            status = respBody["status"]?.toString(),
        )
    }

    override fun updatePlanItem(
        planId: String,
        itemId: String,
        item: PlanItem,
    ): PlanItem {
        val url = "$baseUrl/plans/$planId/items/$itemId"
        val headers = buildHeaders()
        val body =
            mutableMapOf(
                "name" to item.name,
                "description" to item.description,
                "quantity" to item.quantity,
                "cycles" to item.cycles,
                "pricing_scheme" to
                    mapOf(
                        "price" to item.pricingScheme.price,
                        "scheme_type" to item.pricingScheme.schemeType,
                    ),
                "status" to item.status,
            ).filterValues { it != null }

        val response = restTemplate.exchange(url, HttpMethod.PUT, HttpEntity(body, headers), Map::class.java)
        val respBody = response.body as Map<String, Any?>
        return PlanItem(
            id = respBody["id"]?.toString(),
            name = respBody["name"]?.toString() ?: "",
            description = respBody["description"] as? String,
            quantity = (respBody["quantity"] as? Number)?.toInt() ?: 1,
            cycles = (respBody["cycles"] as? Number)?.toInt(),
            pricingScheme =
                PricingScheme(
                    schemeType = (respBody["pricing_scheme"] as? Map<*, *>)?.get("scheme_type")?.toString(),
                    price = (respBody["pricing_scheme"] as? Map<*, *>)?.get("price")?.let { (it as? Number)?.toInt() },
                ),
            status = respBody["status"]?.toString(),
        )
    }

    override fun deletePlanItem(
        planId: String,
        itemId: String,
    ) {
        val url = "$baseUrl/plans/$planId/items/$itemId"
        val headers = buildHeaders()
        restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity(null, headers), Void::class.java)
    }
}
