package com.fitcore.payment.application

import com.fitcore.payment.domain.model.Invoice
import com.fitcore.payment.domain.repository.InvoiceGatewayPort
import org.springframework.stereotype.Service

@Service
class InvoiceService(
    private val invoiceGatewayPort: InvoiceGatewayPort,
) {
    // Cria uma fatura para um ciclo específico da assinatura
    fun createInvoice(
        subscriptionId: String,
        cycleId: String,
        metadata: Map<String, Any?>?,
    ): Invoice {
        return invoiceGatewayPort.createInvoice(subscriptionId, cycleId, metadata)
    }

    // Recupera uma fatura pelo ID
    fun getInvoice(invoiceId: String): Invoice {
        return invoiceGatewayPort.getInvoice(invoiceId)
    }

    // Lista faturas, com todos os filtros opcionais
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

    // Cancela uma fatura
    fun cancelInvoice(invoiceId: String) {
        invoiceGatewayPort.cancelInvoice(invoiceId)
    }
}
