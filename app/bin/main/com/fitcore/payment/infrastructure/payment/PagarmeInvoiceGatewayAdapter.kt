package com.fitcore.payment.infrastructure.payment

import com.fitcore.payment.domain.model.Invoice
import com.fitcore.payment.domain.repository.InvoiceGatewayPort
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Adapter for integrating with Pagar.me's Invoice API endpoints.
 * Handles invoice creation, retrieval, listing and cancellation via HTTP requests.
 */
@Component
class PagarmeInvoiceGatewayAdapter(
    @Value("\${pagarme.api-key}") private val apiKey: String,
    @Value("\${pagarme.base-url}") private val baseUrl: String,
) : InvoiceGatewayPort {

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
     * Creates an invoice for a specific subscription cycle.
     * @param subscriptionId The Pagar.me subscription ID.
     * @param cycleId The cycle number or ID.
     * @param metadata Optional metadata to include in the invoice.
     * @return The created Invoice entity.
     */
    override fun createInvoice(
        subscriptionId: String,
        cycleId: String,
        metadata: Map<String, Any?>?,
    ): Invoice {
        val url = "$baseUrl/subscriptions/$subscriptionId/cycles/$cycleId/pay"
        val headers = buildHeaders()
        val body = mutableMapOf<String, Any?>()
        if (metadata != null) body["metadata"] = metadata

        val response = restTemplate.postForEntity(url, HttpEntity(body, headers), Map::class.java)
        val resp = response.body as Map<String, Any?>

        return mapToInvoice(resp)
    }

    /**
     * Retrieves an invoice by its unique identifier.
     * @param invoiceId The invoice ID.
     * @return The Invoice entity.
     */
    override fun getInvoice(invoiceId: String): Invoice {
        val url = "$baseUrl/invoices/$invoiceId"
        val headers = buildHeaders()

        val response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity(null, headers), Map::class.java)
        val resp = response.body as Map<String, Any?>

        return mapToInvoice(resp)
    }

    /**
     * Lists invoices with optional filters and pagination.
     * All filters are optional.
     * @param status Invoice status to filter by.
     * @param customerId Filter by customer ID.
     * @param subscriptionId Filter by subscription ID.
     * @param dueSince Only invoices due since this date (ISO format).
     * @param dueUntil Only invoices due until this date (ISO format).
     * @param createdSince Only invoices created since this date (ISO format).
     * @param createdUntil Only invoices created until this date (ISO format).
     * @param page Page number for pagination (default: 1).
     * @param size Page size for pagination (default: 10).
     * @return List of Invoice entities matching the filters.
     */
    override fun listInvoices(
        status: String?,
        customerId: String?,
        subscriptionId: String?,
        dueSince: String?,
        dueUntil: String?,
        createdSince: String?,
        createdUntil: String?,
        page: Int,
        size: Int,
    ): List<Invoice> {
        val params = mutableListOf<String>()
        status?.let { params.add("status=$it") }
        customerId?.let { params.add("customer_id=$it") }
        subscriptionId?.let { params.add("subscription_id=$it") }
        dueSince?.let { params.add("due_since=$it") }
        dueUntil?.let { params.add("due_until=$it") }
        createdSince?.let { params.add("created_since=$it") }
        createdUntil?.let { params.add("created_until=$it") }
        params.add("page=$page")
        params.add("size=$size")

        val query = params.joinToString("&")
        val url = "$baseUrl/invoices?$query"
        val headers = buildHeaders()

        val response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity(null, headers), Map::class.java)
        val body = response.body as Map<String, Any?>
        val data = body["data"] as? List<Map<String, Any?>> ?: emptyList()

        return data.map { mapToInvoice(it) }
    }

    /**
     * Cancels an invoice by its unique identifier.
     * @param invoiceId The invoice ID to cancel.
     */
    override fun cancelInvoice(invoiceId: String) {
        val url = "$baseUrl/invoices/$invoiceId"
        val headers = buildHeaders()
        restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity(null, headers), Map::class.java)
    }

    /**
     * Helper to parse ISO datetime strings to LocalDateTime (handles optional "Z" suffix).
     * @param str The ISO date string.
     * @return LocalDateTime or null if parsing fails or input is null.
     */
    private fun parseDateTime(str: Any?): LocalDateTime? =
        (str as? String)?.let {
            try {
                LocalDateTime.parse(it.removeSuffix("Z"), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            } catch (e: Exception) {
                null
            }
        }

    /**
     * Maps a generic response map to the Invoice domain model.
     * Extracts boletoPdfUrl from nested charge.last_transaction.pdf if present.
     * @param map The response map from Pagar.me.
     * @return Invoice domain model.
     */
    private fun mapToInvoice(map: Map<String, Any?>): Invoice {
        val charge = map["charge"] as? Map<*, *>
        val lastTransaction = charge?.get("last_transaction") as? Map<*, *>
        val boletoPdfUrl = lastTransaction?.get("pdf")?.toString()

        return Invoice(
            id = map["id"]?.toString() ?: "",
            url = map["url"]?.toString(),
            amount = (map["amount"] as? Number)?.toInt() ?: 0,
            paymentMethod = map["payment_method"]?.toString() ?: "",
            installments = (map["installments"] as? Number)?.toInt(),
            status = map["status"]?.toString() ?: "",
            billingAt = parseDateTime(map["billing_at"]),
            seenAt = parseDateTime(map["seen_at"]),
            dueAt = parseDateTime(map["due_at"]),
            createdAt = parseDateTime(map["created_at"]),
            canceledAt = parseDateTime(map["canceled_at"]),
            metadata = map["metadata"] as? Map<String, Any>,
            boletoPdfUrl = boletoPdfUrl,
        )
    }
}
