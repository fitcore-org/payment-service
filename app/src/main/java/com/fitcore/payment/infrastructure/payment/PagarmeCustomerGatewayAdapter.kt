package com.fitcore.payment.infrastructure.payment

import com.fitcore.payment.domain.repository.CustomerGatewayPort
import com.fitcore.payment.infrastructure.persistence.entity.RoleEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class PagarmeCustomerGatewayAdapter(
    @Value("\${pagarme.api-key}") private val apiKey: String,
    @Value("\${pagarme.base-url}") private val baseUrl: String,
) : CustomerGatewayPort {
    private val restTemplate = RestTemplate()

    private fun buildHeaders(): HttpHeaders {
        val basicAuth = java.util.Base64.getEncoder().encodeToString("$apiKey:".toByteArray())
        return HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            set("Authorization", "Basic $basicAuth")
            accept = listOf(MediaType.APPLICATION_JSON)
        }
    }

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

    private fun buildPhones(phone: String?): Map<String, Any>? {
        if (phone.isNullOrBlank() || phone.length < 3) return null
        // Espera: DDD + número, ex: 81999998888
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

    override fun deleteCustomer(serviceId: String) {
        val url = "$baseUrl/customers/$serviceId"
        restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity(null, buildHeaders()), Void::class.java)
    }
}
