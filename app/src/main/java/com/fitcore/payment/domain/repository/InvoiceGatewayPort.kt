package com.fitcore.payment.domain.repository

import com.fitcore.payment.domain.model.Invoice

/**
 * Gateway port for invoice-related operations with external providers.
 */
interface InvoiceGatewayPort {
    /**
     * Creates an invoice for a specific subscription cycle.
     * @param subscriptionId The subscription identifier.
     * @param cycleId The cycle identifier or number.
     * @param metadata Optional metadata to associate with the invoice.
     * @return The created Invoice.
     */
    fun createInvoice(
        subscriptionId: String,
        cycleId: String,
        metadata: Map<String, Any?>? = null,
    ): Invoice

    /**
     * Retrieves an invoice by its ID.
     * @param invoiceId The invoice identifier.
     * @return The found Invoice.
     */
    fun getInvoice(invoiceId: String): Invoice

    /**
     * Lists invoices using optional filters and pagination.
     * @param status Filter by invoice status.
     * @param customerId Filter by customer identifier.
     * @param subscriptionId Filter by subscription identifier.
     * @param dueSince Filter by due date since (ISO string).
     * @param dueUntil Filter by due date until (ISO string).
     * @param createdSince Filter by creation date since (ISO string).
     * @param createdUntil Filter by creation date until (ISO string).
     * @param page Page number for pagination.
     * @param size Page size for pagination.
     * @return List of invoices matching the filters.
     */
    fun listInvoices(
        status: String? = null,
        customerId: String? = null,
        subscriptionId: String? = null,
        dueSince: String? = null,
        dueUntil: String? = null,
        createdSince: String? = null,
        createdUntil: String? = null,
        page: Int = 1,
        size: Int = 10,
    ): List<Invoice>

    /**
     * Cancels an invoice by its ID.
     * @param invoiceId The invoice identifier.
     */
    fun cancelInvoice(invoiceId: String)
}
