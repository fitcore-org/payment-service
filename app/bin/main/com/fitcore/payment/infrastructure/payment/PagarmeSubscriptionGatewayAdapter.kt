package com.fitcore.payment.infrastructure.payment

import com.fitcore.payment.domain.model.PricingScheme
import com.fitcore.payment.domain.model.Subscription
import com.fitcore.payment.domain.model.SubscriptionItem
import com.fitcore.payment.domain.repository.RoleRepository
import com.fitcore.payment.domain.repository.SubscriptionGatewayPort
import com.fitcore.payment.infrastructure.persistence.entity.RoleEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.util.*

/**
 * Adapter that translates calls from our domain layer into REST API calls
 * against Pagar.me.  It has been updated to enrich subscription data
 * returned from Pagar.me with the plan’s name and value and to populate
 * the customerId directly from the subscription’s customer object.  For
 * listing subscriptions it additionally fetches plan details so that the
 * returned list includes human friendly information.
 */
@Component
class PagarmeSubscriptionGatewayAdapter(
    @Value("\${pagarme.api-key}") private val apiKey: String,
    @Value("\${pagarme.base-url}") private val baseUrl: String,
    private val roleRepository: RoleRepository,
) : SubscriptionGatewayPort {
    private val restTemplate = RestTemplate()

    private fun buildHeaders(): HttpHeaders {
        val basicAuth = Base64.getEncoder().encodeToString("$apiKey:".toByteArray())
        return HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            set("Authorization", "Basic $basicAuth")
            accept = listOf(MediaType.APPLICATION_JSON)
        }
    }

    private fun buildAddress(role: RoleEntity): Map<String, Any?>? {
        val address = role.address ?: return null
        return mapOf(
            "line_1" to address.line1,
            "line_2" to address.line2,
            "zip_code" to address.zipCode,
            "city" to address.city,
            "state" to address.state,
            "country" to address.country,
        )
    }

    private fun buildPhones(role: RoleEntity): Map<String, Any?>? {
        val phone = role.phone ?: return null
        return mapOf(
            "mobile_phone" to mapOf(
                "country_code" to "55",
                "area_code" to phone.substring(0, 2),
                "number" to phone.substring(2),
            ),
        )
    }

    override fun createSubscription(subscription: Subscription): Subscription {
        val url = "$baseUrl/subscriptions"
        val headers = buildHeaders()

        // 1. Fetch plan details from Pagar.me
        val planUrl = "$baseUrl/plans/${subscription.planId}"
        val planResp = restTemplate.exchange(planUrl, HttpMethod.GET, HttpEntity(null, headers), Map::class.java)
        val planMap = planResp.body as? Map<*, *> ?: throw IllegalStateException("Resposta do plano não é um Map: ${planResp.body}")

        // Extract the pricing scheme value and plan name from the plan
        val planItems = planMap["items"] as? List<*> ?: throw IllegalStateException("Campo 'items' não encontrado ou inválido na resposta do plano: $planMap")
        val firstItem = planItems.firstOrNull() as? Map<*, *> ?: throw IllegalStateException("Plano sem items: $planMap")
        val pricingScheme = firstItem["pricing_scheme"] as? Map<*, *> ?: throw IllegalStateException("Item do plano sem 'pricing_scheme': $firstItem")
        val planAmount = (pricingScheme["price"] as? Number)?.toInt()
            ?: throw IllegalStateException("Campo 'price' não encontrado no 'pricing_scheme': $pricingScheme")
        val planName = planMap["name"]?.toString()

        // 2. Build subscription body
        val body = mutableMapOf<String, Any?>(
            "plan_id" to subscription.planId,
            "payment_method" to subscription.paymentMethod,
            "installments" to subscription.installments,
            "code" to subscription.code,
            "metadata" to subscription.metadata,
            "customer_id" to subscription.customerId,
            "billing" to mapOf(
                "value" to planAmount,
            ),
        )
        if (!subscription.cardId.isNullOrBlank()) {
            body["card_id"] = subscription.cardId
        }
        if (!subscription.cardToken.isNullOrBlank()) {
            body["card_token"] = subscription.cardToken
        }

        val response = restTemplate.postForEntity(url, HttpEntity(body, headers), Map::class.java)
        val resp = response.body as Map<*, *>

        return Subscription(
            id = resp["id"]?.toString(),
            code = resp["code"]?.toString(),
            planId = resp["plan_id"]?.toString() ?: subscription.planId,
            planName = planName,
            planValue = planAmount,
            customerId = subscription.customerId,
            paymentMethod = resp["payment_method"]?.toString() ?: subscription.paymentMethod,
            status = resp["status"]?.toString(),
            startAt = null,
            installments = (resp["installments"] as? Number)?.toInt(),
            metadata = resp["metadata"] as? Map<String, Any>,
            cardId = subscription.cardId,
            cardToken = subscription.cardToken,
        )
    }

    override fun getSubscription(id: String): JsonNode {
        val url = "$baseUrl/subscriptions/$id"
        val headers = buildHeaders()
        val response: ResponseEntity<JsonNode> = restTemplate.exchange(
            url,
            HttpMethod.GET,
            HttpEntity(null, headers),
            JsonNode::class.java
        )
        return response.body!!
    }

    override fun listSubscriptions(): JsonNode {
        val url = "$baseUrl/subscriptions"
        val headers = buildHeaders()
        val response: ResponseEntity<JsonNode> = restTemplate.exchange(
            url,
            HttpMethod.GET,
            HttpEntity(null, headers),
            JsonNode::class.java
        )
        return response.body!!
    }

    override fun cancelSubscription(id: String) {
        val url = "$baseUrl/subscriptions/$id"
        val headers = buildHeaders()
        restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity(null, headers), Map::class.java)
    }

    override fun addItemToSubscription(subscriptionId: String, item: SubscriptionItem): SubscriptionItem {
        val url = "$baseUrl/subscriptions/$subscriptionId/items"
        val headers = buildHeaders()

        val pricingScheme = mutableMapOf<String, Any?>(
            "scheme_type" to item.pricingScheme.schemeType,
            "price" to item.pricingScheme.price,
        ).filterValues { it != null }

        val body = mutableMapOf<String, Any?>(
            "plan_item_id" to item.planItemId,
            "description" to item.description,
            "cycles" to item.cycles,
            "pricing_scheme" to pricingScheme,
            "quantity" to item.quantity,
            "name" to item.name,
        ).filterValues { it != null }

        val response = restTemplate.postForEntity(url, HttpEntity(body, headers), Map::class.java)
        val resp = response.body as Map<*, *>

        return SubscriptionItem(
            id = resp["id"]?.toString(),
            planItemId = resp["plan_item_id"]?.toString(),
            name = resp["name"]?.toString(),
            description = resp["description"]?.toString(),
            pricingScheme = PricingScheme(
                schemeType = (resp["pricing_scheme"] as? Map<*, *>)?.get("scheme_type")?.toString() ?: "",
                price = (resp["pricing_scheme"] as? Map<*, *>)?.get("price") as? Int,
            ),
            cycles = (resp["cycles"] as? Number)?.toInt(),
            quantity = (resp["quantity"] as? Number)?.toInt(),
            status = resp["status"]?.toString(),
            createdAt = resp["created_at"]?.toString(),
            updatedAt = resp["updated_at"]?.toString(),
        )
    }

    override fun listSubscriptionItems(subscriptionId: String): List<SubscriptionItem> {
        val url = "$baseUrl/subscriptions/$subscriptionId/items"
        val headers = buildHeaders()
        val response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity(null, headers), Map::class.java)
        val body = response.body as Map<*, *>
        val data = body["data"] as? List<*> ?: emptyList<Any>()
        return data.map { raw ->
            val resp = raw as Map<*, *>
            SubscriptionItem(
                id = resp["id"]?.toString(),
                planItemId = resp["plan_item_id"]?.toString(),
                name = resp["name"]?.toString(),
                description = resp["description"]?.toString(),
                pricingScheme = PricingScheme(
                    schemeType = (resp["pricing_scheme"] as? Map<*, *>)?.get("scheme_type")?.toString() ?: "",
                    price = (resp["pricing_scheme"] as? Map<*, *>)?.get("price") as? Int,
                ),
                cycles = (resp["cycles"] as? Number)?.toInt(),
                quantity = (resp["quantity"] as? Number)?.toInt(),
                status = resp["status"]?.toString(),
                createdAt = resp["created_at"]?.toString(),
                updatedAt = resp["updated_at"]?.toString(),
            )
        }
    }

    override fun updateSubscriptionItem(subscriptionId: String, itemId: String, item: SubscriptionItem): SubscriptionItem {
        val url = "$baseUrl/subscriptions/$subscriptionId/items/$itemId"
        val headers = buildHeaders()

        val pricingScheme = mutableMapOf<String, Any?>(
            "scheme_type" to item.pricingScheme.schemeType,
            "price" to item.pricingScheme.price,
        ).filterValues { it != null }

        val body = mutableMapOf<String, Any?>(
            "name" to item.name,
            "description" to item.description,
            "cycles" to item.cycles,
            "pricing_scheme" to pricingScheme,
            "quantity" to item.quantity,
            "status" to (item.status ?: "active"),
        ).filterValues { it != null }

        val response = restTemplate.exchange(url, HttpMethod.PUT, HttpEntity(body, headers), Map::class.java)
        val resp = response.body as Map<*, *>
        return SubscriptionItem(
            id = resp["id"]?.toString(),
            planItemId = resp["plan_item_id"]?.toString(),
            name = resp["name"]?.toString(),
            description = resp["description"]?.toString(),
            pricingScheme = PricingScheme(
                schemeType = (resp["pricing_scheme"] as? Map<*, *>)?.get("scheme_type")?.toString() ?: "",
                price = (resp["pricing_scheme"] as? Map<*, *>)?.get("price") as? Int,
            ),
            cycles = (resp["cycles"] as? Number)?.toInt(),
            quantity = (resp["quantity"] as? Number)?.toInt(),
            status = resp["status"]?.toString(),
            createdAt = resp["created_at"]?.toString(),
            updatedAt = resp["updated_at"]?.toString(),
        )
    }

    override fun removeSubscriptionItem(subscriptionId: String, itemId: String) {
        val url = "$baseUrl/subscriptions/$subscriptionId/items/$itemId"
        val headers = buildHeaders()
        restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity(null, headers), Map::class.java)
    }
}