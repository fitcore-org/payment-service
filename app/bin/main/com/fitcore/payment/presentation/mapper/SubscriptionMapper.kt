package com.fitcore.payment.presentation.mapper

import com.fitcore.payment.domain.model.PricingScheme
import com.fitcore.payment.domain.model.Subscription
import com.fitcore.payment.domain.model.SubscriptionItem
import com.fitcore.payment.presentation.dto.PricingSchemeDto
import com.fitcore.payment.presentation.dto.SubscriptionItemRequestDto
import com.fitcore.payment.presentation.dto.SubscriptionItemResponseDto
import com.fitcore.payment.presentation.dto.SubscriptionRequestDto
import com.fitcore.payment.presentation.dto.SubscriptionResponseDto
import java.time.LocalDate

/**
 * Mapper responsible for converting between request/response DTOs and
 * domain models for subscriptions.  It has been updated to include
 * planName and planValue when mapping from the domain model to the
 * response DTO so that these values returned from Pagar.me are
 * propagated back to API callers.
 */
object SubscriptionMapper {
    fun toDomain(dto: SubscriptionRequestDto) =
        Subscription(
            id = null,
            code = dto.code,
            planId = dto.planId,
            customerId = dto.customerId,
            paymentMethod = dto.paymentMethod,
            installments = dto.installments,
            startAt = dto.startAt?.let { LocalDate.parse(it).atStartOfDay() },
            metadata = dto.metadata,
            cardId = dto.cardId,
            cardToken = dto.cardToken,
        )

    fun toResponse(model: Subscription) =
        SubscriptionResponseDto(
            id = model.id ?: "",
            code = model.code,
            planId = model.planId,
            planName = model.planName,
            planValue = model.planValue,
            customerId = model.customerId,
            paymentMethod = model.paymentMethod,
            status = model.status,
            startAt = model.startAt,
            installments = model.installments,
            metadata = model.metadata,
            cardToken = model.cardToken,
        )

    // --------- ITENS DE ASSINATURA -----------

    fun itemToDomain(dto: SubscriptionItemRequestDto) =
        SubscriptionItem(
            id = null,
            planItemId = dto.planItemId,
            name = dto.name,
            description = dto.description,
            pricingScheme =
                PricingScheme(
                    schemeType = dto.pricingScheme.schemeType,
                    price = dto.pricingScheme.price,
                ),
            cycles = dto.cycles,
            quantity = dto.quantity,
            status = null,
            createdAt = null,
            updatedAt = null,
        )

    fun itemToResponse(model: SubscriptionItem) =
        SubscriptionItemResponseDto(
            id = model.id ?: "",
            planItemId = model.planItemId,
            name = model.name,
            description = model.description,
            pricingScheme =
                PricingSchemeDto(
                    schemeType = model.pricingScheme.schemeType,
                    price = model.pricingScheme.price,
                ),
            cycles = model.cycles,
            quantity = model.quantity,
            status = model.status,
            createdAt = model.createdAt,
            updatedAt = model.updatedAt,
        )
}