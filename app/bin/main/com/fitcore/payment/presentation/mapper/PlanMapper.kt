package com.fitcore.payment.presentation.mapper

import com.fitcore.payment.domain.model.Plan
import com.fitcore.payment.domain.model.PlanItem
import com.fitcore.payment.domain.model.PricingScheme
import com.fitcore.payment.presentation.dto.*

/**
 * Mapper object for converting between Plan/PlanItem domain models and DTOs.
 */
object PlanMapper {

    /**
     * Converts PlanRequestDto (API input) to Plan (domain model).
     */
    fun toDomain(dto: PlanRequestDto): Plan =
        Plan(
            id = null,
            name = dto.name,
            description = dto.description,
            shippable = dto.shippable,
            paymentMethods = dto.paymentMethods,
            installments = dto.installments,
            minimumPrice = dto.minimumPrice,
            statementDescriptor = dto.statementDescriptor,
            currency = dto.currency,
            interval = dto.interval,
            intervalCount = dto.intervalCount,
            trialPeriodDays = dto.trialPeriodDays,
            billingType = dto.billingType,
            billingDays = dto.billingDays,
            items = dto.items?.map { toDomainItem(it) },
            metadata = dto.metadata,
            pricingScheme = dto.pricingScheme?.let { toDomainPricingScheme(it) },
            quantity = dto.quantity,
            status = dto.status,
        )

    /**
     * Converts Plan (domain model) to PlanResponseDto (API output).
     */
    fun toResponse(plan: Plan): PlanResponseDto =
        PlanResponseDto(
            id = plan.id ?: "",
            name = plan.name,
            description = plan.description,
            shippable = plan.shippable,
            paymentMethods = plan.paymentMethods,
            installments = plan.installments,
            minimumPrice = plan.minimumPrice,
            statementDescriptor = plan.statementDescriptor,
            currency = plan.currency,
            interval = plan.interval,
            intervalCount = plan.intervalCount,
            trialPeriodDays = plan.trialPeriodDays,
            billingType = plan.billingType,
            billingDays = plan.billingDays,
            items = plan.items?.map { toDtoItem(it) },
            metadata = plan.metadata,
            pricingScheme = plan.pricingScheme?.let { toDtoPricingScheme(it) },
            quantity = plan.quantity,
            status = plan.status,
        )

    /**
     * Converts PlanItemRequestDto (API input for new items) to PlanItem (domain model).
     */
    fun toDomainItem(dto: PlanItemRequestDto): PlanItem =
        PlanItem(
            name = dto.name,
            description = dto.description,
            quantity = dto.quantity,
            pricingScheme = toDomainPricingScheme(dto.pricingScheme),
            cycles = dto.cycles,
            status = null,
        )

    /**
     * Converts PlanItemUpdateDto (API input for update) to PlanItem (domain model).
     */
    fun toDomainItem(dto: PlanItemUpdateDto): PlanItem =
        PlanItem(
            name = dto.name,
            description = dto.description,
            quantity = dto.quantity,
            pricingScheme = toDomainPricingScheme(dto.pricingScheme),
            cycles = dto.cycles,
            status = dto.status,
        )

    /**
     * Converts PlanItem (domain model) to PlanItemDto (API output).
     */
    fun toDtoItem(item: PlanItem): PlanItemDto =
        PlanItemDto(
            id = item.id,
            name = item.name,
            description = item.description,
            quantity = item.quantity,
            pricingScheme = toDtoPricingScheme(item.pricingScheme),
            cycles = item.cycles,
            status = item.status,
        )

    /**
     * Converts PlanItemDto (API output) to PlanItem (domain model).
     * Private, for internal mapper use.
     */
    private fun toDomainItem(dto: PlanItemDto): PlanItem =
        PlanItem(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            quantity = dto.quantity,
            pricingScheme = toDomainPricingScheme(dto.pricingScheme),
            cycles = dto.cycles,
            status = dto.status,
        )

    /**
     * Converts PricingSchemeDto (DTO) to PricingScheme (domain model).
     */
    private fun toDomainPricingScheme(dto: PricingSchemeDto): PricingScheme =
        PricingScheme(
            schemeType = dto.schemeType,
            price = dto.price,
        )

    /**
     * Converts PricingScheme (domain model) to PricingSchemeDto (DTO).
     */
    private fun toDtoPricingScheme(pricingScheme: PricingScheme): PricingSchemeDto =
        PricingSchemeDto(
            schemeType = pricingScheme.schemeType,
            price = pricingScheme.price,
        )
}
