package com.fitcore.payment.application

import com.fitcore.payment.domain.model.Invoice
import com.fitcore.payment.domain.repository.InvoiceGatewayPort
import org.springframework.stereotype.Service

/**
 * Application service for managing invoices.
 * Handles the creation, retrieval, listing, and cancellation of invoices.
 */
@Service
class InvoiceService(
    private val invoiceGatewayPort: InvoiceGatewayPort,
) {
    /**
     * Creates an invoice for a specific subscription cycle.
     * @param subscriptionId The subscription identifier.
     * @param cycleId The cycle identifier or number.
     * @param metadata Optional metadata to be attached to the invoice.
     * @return The created Invoice.
     */
    fun createInvoice(
        subscriptionId: String,
        cycleId: String,
        metadata: Map<String, Any?>?,
    ): Invoice {
        return invoiceGatewayPort.createInvoice(subscriptionId, cycleId, metadata)
    }

    /**
     * Retrieves an invoice by its identifier.
     * @param invoiceId The invoice identifier.
     * @return The found Invoice.
     */
    fun getInvoice(invoiceId: String): Invoice {
        return invoiceGatewayPort.getInvoice(invoiceId)
    }

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
        return invoiceGatewayPort.listInvoices(
            status, customerId, subscriptionId,
            dueSince, dueUntil, createdSince, createdUntil, page, size,
        )
    }

    /**
     * Cancels an invoice by its identifier.
     * @param invoiceId The invoice identifier.
     */
    fun cancelInvoice(invoiceId: String) {
        invoiceGatewayPort.cancelInvoice(invoiceId)
    }
}
