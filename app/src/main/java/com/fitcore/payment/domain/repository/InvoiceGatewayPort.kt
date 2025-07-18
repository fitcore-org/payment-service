package com.fitcore.payment.domain.repository

import com.fitcore.payment.domain.model.Invoice

interface InvoiceGatewayPort {
    fun createInvoice(
        subscriptionId: String,
        cycleId: String,
        metadata: Map<String, Any?>? = null,
    ): Invoice

    fun getInvoice(invoiceId: String): Invoice

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

    fun cancelInvoice(invoiceId: String)
}
