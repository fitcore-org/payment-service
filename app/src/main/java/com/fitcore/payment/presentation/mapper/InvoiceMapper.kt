package com.fitcore.payment.presentation.mapper

import com.fitcore.payment.domain.model.Invoice
import com.fitcore.payment.presentation.dto.CreateInvoiceRequestDto
import com.fitcore.payment.presentation.dto.InvoiceResponseDto

object InvoiceMapper {
    fun toDomain(dto: CreateInvoiceRequestDto): Invoice =
        Invoice(
            id = "",
            url = null,
            amount = 0,
            paymentMethod = "",
            installments = null,
            status = "",
            billingAt = null,
            seenAt = null,
            dueAt = null,
            createdAt = null,
            canceledAt = null,
            metadata = dto.metadata,
        )

    fun toResponse(model: Invoice): InvoiceResponseDto =
        InvoiceResponseDto(
            id = model.id,
            url = model.url,
            amount = model.amount,
            paymentMethod = model.paymentMethod,
            installments = model.installments,
            status = model.status,
            billingAt = model.billingAt,
            seenAt = model.seenAt,
            dueAt = model.dueAt,
            createdAt = model.createdAt,
            canceledAt = model.canceledAt,
            metadata = model.metadata,
            boletoPdfUrl = model.boletoPdfUrl,
        )
}
