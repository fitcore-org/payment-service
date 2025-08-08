package com.fitcore.payment.infrastructure

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class StartupSeeder {

    private val restTemplate = RestTemplate()

    @EventListener(ApplicationReadyEvent::class)
    fun seedUserAndAddress() {

        val phone = "5511999999999"

        val rolePayload = mapOf(
            "name" to "AFF2F Doe",
            "email" to "afaf5a444oe@example.com",
            "phone" to phone, 
            "document" to "12955678999",
            "documentType" to "CPF",
            "type" to "individual",
            "gender" to "MALE",
            "birthdate" to "2000-01-01",
            "roleType" to "MANAGER",
            "address" to mapOf(
                "line1" to "Rua Exemplo 123",
                "line2" to "Apto 101",
                "zipCode" to "12345-678",
                "city" to "São Paulo",
                "state" to "SP",
                "country" to "BR"
            ),
            "serviceId" to null
        )

        val roleResponse = restTemplate.postForEntity(
            "http://localhost:8080/payment/roles",
            rolePayload,
            Map::class.java
        )

        val serviceId = (roleResponse.body?.get("serviceId") as? String) ?: run {
            println("serviceId not found in role response!")
            return
        }

        val addressPayload = mapOf(
            "line1" to "Rua Exemplo 123",
            "line2" to "Apto 101",
            "zipCode" to "12345-678",
            "city" to "São Paulo",
            "state" to "SP",
            "country" to "BR"
        )

        restTemplate.postForEntity(
            "http://localhost:8080/payment/api/customers/$serviceId/addresses",
            addressPayload,
            Map::class.java
        )

        println("User and address seeded!")
    }
}
