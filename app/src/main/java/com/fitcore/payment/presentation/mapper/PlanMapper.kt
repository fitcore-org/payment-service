package com.fitcore.payment.presentation.mapper

import com.fitcore.payment.domain.model.Plan
import com.fitcore.payment.domain.model.PlanItem
import com.fitcore.payment.domain.model.PricingScheme
import com.fitcore.payment.presentation.dto.*

object PlanMapper {
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

    fun toDomainItem(dto: PlanItemRequestDto): PlanItem =
        PlanItem(
            name = dto.name,
            description = dto.description,
            quantity = dto.quantity,
            pricingScheme = toDomainPricingScheme(dto.pricingScheme),
            cycles = dto.cycles,
            status = null,
        )

    fun toDomainItem(dto: PlanItemUpdateDto): PlanItem =
        PlanItem(
            name = dto.name,
            description = dto.description,
            quantity = dto.quantity,
            pricingScheme = toDomainPricingScheme(dto.pricingScheme),
            cycles = dto.cycles,
            status = dto.status,
        )

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

    private fun toDomainPricingScheme(dto: PricingSchemeDto): PricingScheme =
        PricingScheme(
            schemeType = dto.schemeType,
            price = dto.price,
        )

    private fun toDtoPricingScheme(pricingScheme: PricingScheme): PricingSchemeDto =
        PricingSchemeDto(
            schemeType = pricingScheme.schemeType,
            price = pricingScheme.price,
        )
}
