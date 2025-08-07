package com.fitcore.payment.infrastructure.payment

import com.fitcore.payment.domain.model.Address
import com.fitcore.payment.domain.repository.AddressGatewayPort
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

/**
 * Adapter for integrating with Pagar.me's Address API endpoints.
 * Handles all address-related operations for a customer via HTTP requests.
 */
@Component
class PagarmeAddressGatewayAdapter(
    @Value("\${pagarme.api-key}") private val apiKey: String,
    @Value("\${pagarme.base-url}") private val baseUrl: String,
) : AddressGatewayPort {

    private val restTemplate = RestTemplate()

    /**
     * Builds the HTTP headers for Pagar.me API requests, including authentication.
     * @return Configured HttpHeaders with JSON content type and Basic Auth.
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
     * Creates a new address for the specified customer.
     * @param customerId The Pagar.me customer ID.
     * @param address The address data to create.
     * @return The created Address entity.
     */
    override fun createAddress(customerId: String, address: Address): Address {
        val url = "$baseUrl/customers/$customerId/addresses"
        val body = mutableMapOf<String, Any?>(
            "line_1" to address.line1,
            "line_2" to address.line2,
            "zip_code" to address.zipCode,
            "city" to address.city,
            "state" to address.state,
            "country" to address.country
        ).filterValues { it != null }

        val response = restTemplate.postForEntity(url, HttpEntity(body, buildHeaders()), Map::class.java)
        val resp = response.body as Map<String, Any?>

        return Address(
            id = resp["id"]?.toString(),
            line1 = resp["line_1"]?.toString() ?: address.line1,
            line2 = resp["line_2"]?.toString(),
            zipCode = resp["zip_code"]?.toString() ?: address.zipCode,
            city = resp["city"]?.toString() ?: address.city,
            state = resp["state"]?.toString() ?: address.state,
            country = resp["country"]?.toString() ?: address.country,
        )
    }

    /**
     * Retrieves an address for a given customer by its ID.
     * @param customerId The Pagar.me customer ID.
     * @param addressId The address ID.
     * @return The requested Address entity.
     */
    override fun getAddress(customerId: String, addressId: String): Address {
        val url = "$baseUrl/customers/$customerId/addresses/$addressId"
        val response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity(null, buildHeaders()), Map::class.java)
        val resp = response.body as Map<String, Any?>

        return Address(
            id = resp["id"]?.toString(),
            line1 = resp["line_1"]?.toString() ?: "",
            line2 = resp["line_2"]?.toString(),
            zipCode = resp["zip_code"]?.toString() ?: "",
            city = resp["city"]?.toString() ?: "",
            state = resp["state"]?.toString() ?: "",
            country = resp["country"]?.toString() ?: "",
        )
    }

    /**
     * Updates an existing address for a given customer.
     * Only line_2 (complement) can be updated on Pagar.me.
     * @param customerId The Pagar.me customer ID.
     * @param addressId The address ID to update.
     * @param address The new address data (only line2 is considered).
     * @return The updated Address entity.
     */
    override fun updateAddress(customerId: String, addressId: String, address: Address): Address {
        val url = "$baseUrl/customers/$customerId/addresses/$addressId"
        val body = mutableMapOf<String, Any?>(
            "line_2" to address.line2,
        ).filterValues { it != null }

        val response = restTemplate.exchange(url, HttpMethod.PUT, HttpEntity(body, buildHeaders()), Map::class.java)
        val resp = response.body as Map<String, Any?>

        return Address(
            id = resp["id"]?.toString(),
            line1 = resp["line_1"]?.toString() ?: "",
            line2 = resp["line_2"]?.toString(),
            zipCode = resp["zip_code"]?.toString() ?: "",
            city = resp["city"]?.toString() ?: "",
            state = resp["state"]?.toString() ?: "",
            country = resp["country"]?.toString() ?: "",
        )
    }

    /**
     * Lists all addresses associated with the specified customer.
     * @param customerId The Pagar.me customer ID.
     * @return List of Address entities.
     */
    override fun listAddresses(customerId: String): List<Address> {
        val url = "$baseUrl/customers/$customerId/addresses"
        val response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity(null, buildHeaders()), Map::class.java)
        val body = response.body as Map<String, Any>
        val data = body["data"] as? List<Map<String, Any?>> ?: emptyList()

        return data.map { resp ->
            Address(
                id = resp["id"]?.toString(),
                line1 = resp["line_1"]?.toString() ?: "",
                line2 = resp["line_2"]?.toString(),
                zipCode = resp["zip_code"]?.toString() ?: "",
                city = resp["city"]?.toString() ?: "",
                state = resp["state"]?.toString() ?: "",
                country = resp["country"]?.toString() ?: "",
            )
        }
    }

    /**
     * Deletes an address for the given customer by its ID.
     * @param customerId The Pagar.me customer ID.
     * @param addressId The address ID to delete.
     */
    override fun deleteAddress(customerId: String, addressId: String) {
        val url = "$baseUrl/customers/$customerId/addresses/$addressId"
        restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity(null, buildHeaders()), Void::class.java)
    }
}
