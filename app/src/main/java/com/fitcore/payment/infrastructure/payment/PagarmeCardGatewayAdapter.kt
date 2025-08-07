package com.fitcore.payment.infrastructure.payment

import com.fitcore.payment.domain.repository.CardGatewayPort
import com.fitcore.payment.presentation.dto.CardRequestDto
import com.fitcore.payment.presentation.dto.CardResponseDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.util.*

/**
 * Adapter for integrating with Pagar.me's Card API endpoints.
 * Handles all card-related operations for a customer via HTTP requests.
 */
@Component
class PagarmeCardGatewayAdapter(
    @Value("\${pagarme.api-key}") private val apiKey: String,
    @Value("\${pagarme.base-url}") private val baseUrl: String,
) : CardGatewayPort {

    private val restTemplate = RestTemplate()

    /**
     * Builds the HTTP headers for Pagar.me API requests, including authentication.
     * @return Configured HttpHeaders with JSON content type and Basic Auth.
     */
    private fun buildHeaders(): HttpHeaders {
        val basicAuth = Base64.getEncoder().encodeToString("$apiKey:".toByteArray())
        return HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            set("Authorization", "Basic $basicAuth")
        }
    }

    /**
     * Creates a new card for the given customer.
     * Fetches the customer's billing address and uses its ID as billing_address_id.
     * @param customerPagarmeId The Pagar.me customer ID.
     * @param dto The card data to create.
     * @return The created CardResponseDto.
     * @throws RuntimeException if no billing address is found for the customer.
     */
    override fun createCard(
        customerPagarmeId: String,
        dto: CardRequestDto,
    ): CardResponseDto {
        val url = "$baseUrl/customers/$customerPagarmeId/cards"

        // Fetch customer addresses to get a billing address
        val addressesUrl = "$baseUrl/customers/$customerPagarmeId/addresses"
        val addressesResponse = restTemplate.exchange(
            addressesUrl,
            HttpMethod.GET,
            HttpEntity(null, buildHeaders()),
            Map::class.java
        )
        val addressesBody = addressesResponse.body as Map<String, Any?>
        val addressData = addressesBody["data"] as? List<Map<String, Any?>> ?: emptyList()
        val billingAddressId = addressData.firstOrNull()?.get("id")?.toString()
        if (billingAddressId == null) {
            throw RuntimeException("No billing address found for customer")
        }

        // Build body with billing_address_id
        val body = mutableMapOf<String, Any?>(
            "number" to dto.number,
            "holder_name" to dto.holderName,
            "exp_month" to dto.expMonth,
            "exp_year" to dto.expYear,
            "cvv" to dto.cvv,
            "billing_address_id" to billingAddressId
        )
        dto.label?.let { body["label"] = it }
        dto.holderDocument?.let { body["holder_document"] = it }
        dto.brand?.let { body["brand"] = it }

        val response = restTemplate.postForEntity(url, HttpEntity(body, buildHeaders()), Map::class.java)
        val resp = response.body as Map<String, Any?>

        return CardResponseDto(
            id = resp["id"].toString(),
            holderName = resp["holder_name"]?.toString() ?: "",
            brand = resp["brand"]?.toString(),
            lastFourDigits = resp["last_four_digits"]?.toString() ?: "",
            expMonth = (resp["exp_month"] as? Number)?.toInt() ?: 0,
            expYear = (resp["exp_year"] as? Number)?.toInt() ?: 0,
            label = resp["label"]?.toString(),
            createdAt = resp["created_at"]?.toString(),
        )
    }

    /**
     * Lists all cards for the given customer.
     * @param customerPagarmeId The Pagar.me customer ID.
     * @return List of CardResponseDto.
     */
    override fun listCards(customerPagarmeId: String): List<CardResponseDto> {
        val url = "$baseUrl/customers/$customerPagarmeId/cards"
        val response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity(null, buildHeaders()), Map::class.java)
        val resp = response.body as Map<String, Any?>
        val data = resp["data"] as? List<Map<String, Any?>> ?: emptyList()

        return data.map {
            CardResponseDto(
                id = it["id"].toString(),
                holderName = it["holder_name"]?.toString() ?: "",
                brand = it["brand"]?.toString(),
                lastFourDigits = it["last_four_digits"]?.toString() ?: "",
                expMonth = (it["exp_month"] as? Number)?.toInt() ?: 0,
                expYear = (it["exp_year"] as? Number)?.toInt() ?: 0,
                label = it["label"]?.toString(),
                createdAt = it["created_at"]?.toString(),
            )
        }
    }

    /**
     * Retrieves a card by its ID for the given customer.
     * @param customerPagarmeId The Pagar.me customer ID.
     * @param cardId The card ID to retrieve.
     * @return CardResponseDto with card details.
     */
    override fun getCard(
        customerPagarmeId: String,
        cardId: String,
    ): CardResponseDto {
        val url = "$baseUrl/customers/$customerPagarmeId/cards/$cardId"
        val resp = restTemplate.exchange(url, HttpMethod.GET, HttpEntity(null, buildHeaders()), Map::class.java).body as Map<String, Any?>
        return CardResponseDto(
            id = resp["id"].toString(),
            holderName = resp["holder_name"]?.toString() ?: "",
            brand = resp["brand"]?.toString(),
            lastFourDigits = resp["last_four_digits"]?.toString() ?: "",
            expMonth = (resp["exp_month"] as? Number)?.toInt() ?: 0,
            expYear = (resp["exp_year"] as? Number)?.toInt() ?: 0,
            label = resp["label"]?.toString(),
            createdAt = resp["created_at"]?.toString(),
        )
    }

    /**
     * Deletes a card by its ID for the given customer.
     * @param customerPagarmeId The Pagar.me customer ID.
     * @param cardId The card ID to delete.
     */
    override fun deleteCard(
        customerPagarmeId: String,
        cardId: String,
    ) {
        val url = "$baseUrl/customers/$customerPagarmeId/cards/$cardId"
        restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity(null, buildHeaders()), Map::class.java)
    }

    /**
     * Updates a card for the given customer and card ID.
     * Only allows updating holder_name, exp_month, exp_year, label, holder_document and brand.
     * @param customerPagarmeId The Pagar.me customer ID.
     * @param cardId The card ID to update.
     * @param dto The card data to update.
     * @return The updated CardResponseDto.
     */
    override fun updateCard(
        customerPagarmeId: String,
        cardId: String,
        dto: CardRequestDto,
    ): CardResponseDto {
        val url = "$baseUrl/customers/$customerPagarmeId/cards/$cardId"
        val body = mutableMapOf<String, Any?>(
            "holder_name" to dto.holderName,
            "exp_month" to dto.expMonth,
            "exp_year" to dto.expYear,
        )
        dto.label?.let { body["label"] = it }
        dto.holderDocument?.let { body["holder_document"] = it }
        dto.brand?.let { body["brand"] = it }
        val response = restTemplate.exchange(url, HttpMethod.PUT, HttpEntity(body, buildHeaders()), Map::class.java)
        val resp = response.body as Map<String, Any?>

        return CardResponseDto(
            id = resp["id"].toString(),
            holderName = resp["holder_name"]?.toString() ?: "",
            brand = resp["brand"]?.toString(),
            lastFourDigits = resp["last_four_digits"]?.toString() ?: "",
            expMonth = (resp["exp_month"] as? Number)?.toInt() ?: 0,
            expYear = (resp["exp_year"] as? Number)?.toInt() ?: 0,
            label = resp["label"]?.toString(),
            createdAt = resp["created_at"]?.toString(),
        )
    }
}
