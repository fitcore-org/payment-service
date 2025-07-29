package com.fitcore.payment.infrastructure.payment

import com.fitcore.payment.domain.model.Address
import com.fitcore.payment.domain.repository.AddressGatewayPort
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class PagarmeAddressGatewayAdapter(
    @Value("\${pagarme.api-key}") private val apiKey: String,
    @Value("\${pagarme.base-url}") private val baseUrl: String,
) : AddressGatewayPort {
    private val restTemplate = RestTemplate()

    private fun buildHeaders(): HttpHeaders {
        val basicAuth = java.util.Base64.getEncoder().encodeToString("$apiKey:".toByteArray())
        return HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            set("Authorization", "Basic $basicAuth")
            accept = listOf(MediaType.APPLICATION_JSON)
        }
    }

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

    override fun deleteAddress(customerId: String, addressId: String) {
        val url = "$baseUrl/customers/$customerId/addresses/$addressId"
        restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity(null, buildHeaders()), Void::class.java)
    }
}
