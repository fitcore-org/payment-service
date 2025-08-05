package com.fitcore.payment.infrastructure.payment

import com.fitcore.payment.domain.model.PricingScheme
import com.fitcore.payment.domain.model.Subscription
import com.fitcore.payment.domain.model.SubscriptionItem
import com.fitcore.payment.domain.repository.RoleRepository
import com.fitcore.payment.domain.repository.SubscriptionGatewayPort
import com.fitcore.payment.infrastructure.persistence.entity.RoleEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.util.*

/**
 * Adapter for integrating with Pagar.me's Subscription API endpoints.
 * Handles subscription and subscription item operations via HTTP requests.
 */
@Component
class PagarmeSubscriptionGatewayAdapter(
    @Value("\${pagarme.api-key}") private val apiKey: String,
    @Value("\${pagarme.base-url}") private val baseUrl: String,
    private val roleRepository: RoleRepository,
) : SubscriptionGatewayPort {

    private val restTemplate = RestTemplate()

    /**
     * Builds HTTP headers for Pagar.me API requests, including Basic Auth.
     * @return Configured HttpHeaders with JSON content type and authentication.
     */
    private fun buildHeaders(): HttpHeaders {
        val basicAuth = java.util.Base64.getEncoder().encodeToString("$apiKey:".toByteArray())
        return HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            set("Authorization", "Basic $basicAuth")
            accept = listOf(MediaType.APPLICATION_JSON)
        }
    }

    /**
     * Builds an address payload from a RoleEntity.
     * @param role The RoleEntity containing the address information.
     * @return A map representing the address or null if not present.
     */
    private fun buildAddress(role: RoleEntity): Map<String, Any?>? {
        val address = role.address ?: return null
        return mapOf(
            "line_1" to address.line1,
            "line_2" to address.line2,
            "zip_code" to address.zipCode,
            "city" to address.city,
            "state" to address.state,
            "country" to address.country
        )
    }

    /**
     * Builds the phones payload from a RoleEntity.
     * @param role The RoleEntity containing the phone number.
     * @return A map representing the phone or null if not present.
     */
    private fun buildPhones(role: RoleEntity): Map<String, Any?>? {
        val phone = role.phone ?: return null
        return mapOf(
            "mobile_phone" to mapOf(
                "country_code" to "55",
                "area_code" to phone.substring(0, 2),
                "number" to phone.substring(2)
            )
        )
    }

    /**
     * Creates a new subscription.
     * Fetches the plan value from Pagar.me and uses it for billing.
     * @param subscription The Subscription domain model.
     * @return The created Subscription.
     * @throws IllegalStateException if plan or pricing data is invalid.
     */
    override fun createSubscription(subscription: Subscription): Subscription {
        val url = "$baseUrl/subscriptions"
        val headers = buildHeaders()

        // Fetch the plan's pricing value
        val planUrl = "$baseUrl/plans/${subscription.planId}"
        val planResp = restTemplate.exchange(planUrl, HttpMethod.GET, HttpEntity(null, headers), Map::class.java)
        val planMap = planResp.body as? Map<String, Any?>
            ?: throw IllegalStateException("Plan response is not a valid Map: ${planResp.body}")

        val items = planMap["items"] as? List<*>
            ?: throw IllegalStateException("Field 'items' missing or invalid in plan response: $planMap")
        val firstItem = items.firstOrNull() as? Map<*, *>
            ?: throw IllegalStateException("Plan has no items: $planMap")
        val pricingScheme = firstItem["pricing_scheme"] as? Map<*, *>
            ?: throw IllegalStateException("Plan item missing 'pricing_scheme': $firstItem")
        val planAmount = (pricingScheme["price"] as? Number)?.toInt()
            ?: throw IllegalStateException("Field 'price' not found in 'pricing_scheme': $pricingScheme")

        // Build the subscription body
        val body = mutableMapOf<String, Any?>(
            "plan_id" to subscription.planId,
            "payment_method" to subscription.paymentMethod,
            "installments" to subscription.installments,
            "code" to subscription.code,
            "metadata" to subscription.metadata,
            "customer_id" to subscription.customerId,
            "billing" to mapOf(
                "value" to planAmount
            )
        )

        if (!subscription.cardId.isNullOrBlank()) {
            body["card_id"] = subscription.cardId
        }
        if (!subscription.cardToken.isNullOrBlank()) {
            body["card_token"] = subscription.cardToken
        }

        val response = restTemplate.postForEntity(url, HttpEntity(body, headers), Map::class.java)
        val resp = response.body as Map<String, Any?>

        return Subscription(
            id = resp["id"]?.toString(),
            code = resp["code"]?.toString(),
            planId = resp["plan_id"]?.toString() ?: subscription.planId,
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

    /**
     * Retrieves a subscription by its ID.
     * @param id The subscription ID.
     * @return The Subscription.
     */
    override fun getSubscription(id: String): Subscription {
        val url = "$baseUrl/subscriptions/$id"
        val headers = buildHeaders()
        val response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity(null, headers), Map::class.java)
        val resp = response.body as Map<String, Any?>

        return Subscription(
            id = resp["id"]?.toString(),
            code = resp["code"]?.toString(),
            planId = resp["plan_id"]?.toString() ?: "",
            customerId = "",
            paymentMethod = resp["payment_method"]?.toString() ?: "",
            status = resp["status"]?.toString(),
            startAt = null,
            installments = (resp["installments"] as? Number)?.toInt(),
            metadata = resp["metadata"] as? Map<String, Any>,
        )
    }

    /**
     * Lists all subscriptions.
     * @return List of all Subscription entities.
     */
    override fun listSubscriptions(): List<Subscription> {
        val url = "$baseUrl/subscriptions"
        val headers = buildHeaders()
        val response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity(null, headers), Map::class.java)
        val body = response.body as Map<String, Any>
        val data = body["data"] as? List<Map<String, Any?>> ?: emptyList()

        return data.map {
            Subscription(
                id = it["id"]?.toString(),
                code = it["code"]?.toString(),
                planId = it["plan_id"]?.toString() ?: "",
                customerId = "",
                paymentMethod = it["payment_method"]?.toString() ?: "",
                status = it["status"]?.toString(),
                startAt = null,
                installments = (it["installments"] as? Number)?.toInt(),
                metadata = it["metadata"] as? Map<String, Any>,
            )
        }
    }

    /**
     * Cancels a subscription by its ID.
     * @param id The subscription ID.
     */
    override fun cancelSubscription(id: String) {
        val url = "$baseUrl/subscriptions/$id"
        val headers = buildHeaders()
        restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity(null, headers), Map::class.java)
    }

    /**
     * Adds an item to a subscription.
     * @param subscriptionId The subscription ID.
     * @param item The SubscriptionItem to add.
     * @return The created SubscriptionItem.
     */
    override fun addItemToSubscription(
        subscriptionId: String,
        item: SubscriptionItem,
    ): SubscriptionItem {
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
        val resp = response.body as Map<String, Any?>

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

    /**
     * Lists all items for a subscription.
     * @param subscriptionId The subscription ID.
     * @return List of SubscriptionItem entities.
     */
    override fun listSubscriptionItems(subscriptionId: String): List<SubscriptionItem> {
        val url = "$baseUrl/subscriptions/$subscriptionId/items"
        val headers = buildHeaders()
        val response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity(null, headers), Map::class.java)
        val body = response.body as Map<String, Any?>
        val data = body["data"] as? List<Map<String, Any?>> ?: emptyList()

        return data.map { resp ->
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

    /**
     * Updates an item in a subscription.
     * @param subscriptionId The subscription ID.
     * @param itemId The item ID.
     * @param item The SubscriptionItem with new data.
     * @return The updated SubscriptionItem.
     */
    override fun updateSubscriptionItem(
        subscriptionId: String,
        itemId: String,
        item: SubscriptionItem,
    ): SubscriptionItem {
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
        val resp = response.body as Map<String, Any?>

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

    /**
     * Removes an item from a subscription by its ID.
     * @param subscriptionId The subscription ID.
     * @param itemId The item ID to remove.
     */
    override fun removeSubscriptionItem(
        subscriptionId: String,
        itemId: String,
    ) {
        val url = "$baseUrl/subscriptions/$subscriptionId/items/$itemId"
        val headers = buildHeaders()
        restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity(null, headers), Map::class.java)
    }
}
