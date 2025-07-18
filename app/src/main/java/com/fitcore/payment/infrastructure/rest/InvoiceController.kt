package com.fitcore.payment.presentation.rest

import com.fitcore.payment.application.InvoiceService
import com.fitcore.payment.presentation.dto.CreateInvoiceRequestDto
import com.fitcore.payment.presentation.dto.InvoiceResponseDto
import com.fitcore.payment.presentation.mapper.InvoiceMapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/invoices")
class InvoiceController(
    private val invoiceService: InvoiceService,
) {
    // Criar fatura para um ciclo de assinatura específico
    @PostMapping("/subscriptions/{subscriptionId}/cycles/{cycleId}/pay")
    fun createInvoice(
        @PathVariable subscriptionId: String,
        @PathVariable cycleId: String,
        @RequestBody(required = false) dto: CreateInvoiceRequestDto?,
    ): ResponseEntity<InvoiceResponseDto> {
        val invoice = invoiceService.createInvoice(subscriptionId, cycleId, dto?.metadata)
        return ResponseEntity.ok(InvoiceMapper.toResponse(invoice))
    }

    // Obter fatura por ID
    @GetMapping("/{invoiceId}")
    fun getInvoice(
        @PathVariable invoiceId: String,
    ): ResponseEntity<InvoiceResponseDto> {
        val invoice = invoiceService.getInvoice(invoiceId)
        return ResponseEntity.ok(InvoiceMapper.toResponse(invoice))
    }

    // Listar faturas com filtros (todos opcionais)
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

    // Cancelar fatura
    @DeleteMapping("/{invoiceId}")
    fun cancelInvoice(
        @PathVariable invoiceId: String,
    ): ResponseEntity<Void> {
        invoiceService.cancelInvoice(invoiceId)
        return ResponseEntity.noContent().build()
    }
}
