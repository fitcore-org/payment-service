package com.fitcore.payment.presentation.rest

import com.fitcore.payment.application.InvoiceService
import com.fitcore.payment.presentation.dto.CreateInvoiceRequestDto
import com.fitcore.payment.presentation.dto.InvoiceResponseDto
import com.fitcore.payment.presentation.mapper.InvoiceMapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST controller responsible for managing invoices.
 * All endpoints are documented and return responses in English.
 */
@RestController
@RequestMapping("/api/invoices")
class InvoiceController(
    private val invoiceService: InvoiceService,
) {

    /**
     * Creates a new invoice for a specific subscription cycle.
     * @param subscriptionId The ID of the subscription.
     * @param cycleId The cycle number or ID for which the invoice will be created.
     * @param dto Optional metadata or additional invoice details.
     * @return The created invoice.
     */
    @PostMapping("/subscriptions/{subscriptionId}/cycles/{cycleId}/pay")
    fun createInvoice(
        @PathVariable subscriptionId: String,
        @PathVariable cycleId: String,
        @RequestBody(required = false) dto: CreateInvoiceRequestDto?,
    ): ResponseEntity<InvoiceResponseDto> {
        val invoice = invoiceService.createInvoice(subscriptionId, cycleId, dto?.metadata)
        return ResponseEntity.ok(InvoiceMapper.toResponse(invoice))
    }

    /**
     * Retrieves an invoice by its unique identifier.
     * @param invoiceId The ID of the invoice to retrieve.
     * @return The invoice data.
     */
    @GetMapping("/{invoiceId}")
    fun getInvoice(
        @PathVariable invoiceId: String,
    ): ResponseEntity<InvoiceResponseDto> {
        val invoice = invoiceService.getInvoice(invoiceId)
        return ResponseEntity.ok(InvoiceMapper.toResponse(invoice))
    }

    /**
     * Lists invoices with optional filters.
     * All filters are optional and can be combined.
     * @param status Invoice status to filter by.
     * @param customerId Filter by customer ID.
     * @param subscriptionId Filter by subscription ID.
     * @param dueSince Only invoices due since this date (ISO format).
     * @param dueUntil Only invoices due until this date (ISO format).
     * @param createdSince Only invoices created since this date (ISO format).
     * @param createdUntil Only invoices created until this date (ISO format).
     * @param page The page number for pagination (default: 1).
     * @param size The page size for pagination (default: 10).
     * @return List of invoices matching the filters.
     */
    @GetMapping
    fun listInvoices(
        @RequestParam(required = false) status: String?,
        @RequestParam(required = false) customerId: String?,
        @RequestParam(required = false) subscriptionId: String?,
        @RequestParam(required = false) dueSince: String?,
        @RequestParam(required = false) dueUntil: String?,
        @RequestParam(required = false) createdSince: String?,
        @RequestParam(required = false) createdUntil: String?,
        @RequestParam(required = false, defaultValue = "1") page: Int,
        @RequestParam(required = false, defaultValue = "10") size: Int,
    ): ResponseEntity<List<InvoiceResponseDto>> {
        val invoices =
            invoiceService.listInvoices(
                status, customerId, subscriptionId,
                dueSince, dueUntil, createdSince, createdUntil, page, size,
            )
        return ResponseEntity.ok(invoices.map { InvoiceMapper.toResponse(it) })
    }

    /**
     * Cancels an invoice by its unique identifier.
     * @param invoiceId The ID of the invoice to cancel.
     * @return No content if successfully cancelled.
     */
    @DeleteMapping("/{invoiceId}")
    fun cancelInvoice(
        @PathVariable invoiceId: String,
    ): ResponseEntity<Void> {
        invoiceService.cancelInvoice(invoiceId)
        return ResponseEntity.noContent().build()
    }
}
