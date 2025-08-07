package com.fitcore.payment.infrastructure.payment

import com.fitcore.payment.domain.repository.CustomerGatewayPort
import com.fitcore.payment.infrastructure.persistence.entity.RoleEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

/**
 * Adapter for integrating with Pagar.me's Customer API endpoints.
 * Handles customer creation, update and deletion via HTTP requests.
 */
@Component
class PagarmeCustomerGatewayAdapter(
    @Value("\${pagarme.api-key}") private val apiKey: String,
    @Value("\${pagarme.base-url}") private val baseUrl: String,
) : CustomerGatewayPort {

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
     * Builds the addresses payload for the customer creation/update request.
     * @param role The RoleEntity containing the address information.
     * @return List of address maps or null if no address is set.
     */
    private fun buildAddresses(role: RoleEntity): List<Map<String, Any?>>? {
        return role.address?.let {
            listOf(
                mapOf(
                    "line_1" to it.line1,
                    "line_2" to it.line2,
                    "zip_code" to it.zipCode,
                    "city" to it.city,
                    "state" to it.state,
                    "country" to it.country
                )
            )
        }
    }

    /**
     * Builds the phones payload for the customer creation/update request.
     * Expects phone in the format DDD + number (e.g. 81999998888).
     * @param phone The phone string.
     * @return Map with phone structure or null if phone is invalid/blank.
     */
    private fun buildPhones(phone: String?): Map<String, Any>? {
        if (phone.isNullOrBlank() || phone.length < 3) return null
        val areaCode = phone.substring(0, 2)
        val number = phone.substring(2)
        return mapOf(
            "mobile_phone" to mapOf(
                "country_code" to "55",
                "area_code" to areaCode,
                "number" to number
            )
        )
    }

    /**
     * Creates a customer on Pagar.me using data from RoleEntity.
     * @param role The role/entity containing customer data.
     * @return The ID of the newly created customer on Pagar.me.
     * @throws RuntimeException if Pagar.me response does not contain an ID.
     */
    override fun createCustomer(role: RoleEntity): String {
        val url = "$baseUrl/customers"
        val addresses = buildAddresses(role)
        val phones = buildPhones(role.phone)

        val body = mutableMapOf<String, Any?>(
            "name" to role.name,
            "email" to role.email,
            "document" to role.document,
            "document_type" to role.documentType,
            "type" to (role.type ?: "individual"),
            "gender" to role.gender,
            "phones" to phones,
            "birthdate" to role.birthdate,
        ).apply {
            if (addresses != null) put("addresses", addresses)
        }.filterValues { it != null }

        val response = restTemplate.postForEntity(url, HttpEntity(body, buildHeaders()), Map::class.java)
        val resp = response.body as Map<String, Any?>
        return resp["id"]?.toString() ?: throw RuntimeException("Missing id from Pagar.me")
    }

    /**
     * Updates an existing customer on Pagar.me with data from RoleEntity.
     * @param serviceId The ID of the customer to update.
     * @param role The role/entity with updated customer data.
     */
    override fun updateCustomer(
        serviceId: String,
        role: RoleEntity,
    ) {
        val url = "$baseUrl/customers/$serviceId"
        val addresses = buildAddresses(role)
        val phones = buildPhones(role.phone)

        val body = mutableMapOf<String, Any?>(
            "name" to role.name,
            "email" to role.email,
            "document" to role.document,
            "document_type" to role.documentType,
            "type" to (role.type ?: "individual"),
            "gender" to role.gender,
            "phones" to phones,
            "birthdate" to role.birthdate,
        ).apply {
            if (addresses != null) put("addresses", addresses)
        }.filterValues { it != null }

        restTemplate.exchange(url, HttpMethod.PUT, HttpEntity(body, buildHeaders()), Map::class.java)
    }

    /**
     * Deletes a customer from Pagar.me by its ID.
     * @param serviceId The ID of the customer to delete.
     */
    override fun deleteCustomer(serviceId: String) {
        val url = "$baseUrl/customers/$serviceId"
        restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity(null, buildHeaders()), Void::class.java)
    }
}
