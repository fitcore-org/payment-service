package com.fitcore.payment.presentation.rest

import com.fitcore.payment.application.SubscriptionService
import com.fitcore.payment.infrastructure.payment.PagarmeSubscriptionGatewayAdapter
import com.fitcore.payment.presentation.dto.SubscriptionItemRequestDto
import com.fitcore.payment.presentation.dto.SubscriptionItemResponseDto
import com.fitcore.payment.presentation.dto.SubscriptionRequestDto
import com.fitcore.payment.presentation.mapper.SubscriptionMapper
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST controller responsible for managing Subscriptions and Subscription Items.
 * All endpoints return messages and responses in English.
 */
@RestController
@RequestMapping("/payment/api/subscriptions")
class SubscriptionController(
    private val subscriptionService: SubscriptionService,
    private val pagarmeSubscriptionGatewayAdapter: PagarmeSubscriptionGatewayAdapter, // <- adapter injetado
) {

    @PostMapping
    fun createSubscription(
        @RequestBody dto: SubscriptionRequestDto,
    ): ResponseEntity<*> =
        ResponseEntity.ok(
            SubscriptionMapper.toResponse(
                subscriptionService.createSubscription(SubscriptionMapper.toDomain(dto)),
            ),
        )

    @GetMapping("/{id}")
    fun getSubscription(@PathVariable id: String): ResponseEntity<JsonNode> {
        val result = pagarmeSubscriptionGatewayAdapter.getSubscription(id)
        return ResponseEntity.ok(result)
    }

    @GetMapping
    fun listSubscriptions(): ResponseEntity<JsonNode> {
        val result = pagarmeSubscriptionGatewayAdapter.listSubscriptions()
        return ResponseEntity.ok(result)
    }

    @DeleteMapping("/{id}")
    fun cancelSubscription(
        @PathVariable id: String,
    ): ResponseEntity<Void> {
        subscriptionService.cancelSubscription(id)
        return ResponseEntity.noContent().build()
    }

    // -------- SUBSCRIPTION ITEM ROUTES ----------

    @PostMapping("/{subscriptionId}/items")
    fun addItemToSubscription(
        @PathVariable subscriptionId: String,
        @RequestBody dto: SubscriptionItemRequestDto,
    ): ResponseEntity<SubscriptionItemResponseDto> {
        val item = subscriptionService.addItemToSubscription(
            subscriptionId,
            SubscriptionMapper.itemToDomain(dto),
        )
        return ResponseEntity.ok(SubscriptionMapper.itemToResponse(item))
    }

    @GetMapping("/{subscriptionId}/items")
    fun listSubscriptionItems(
        @PathVariable subscriptionId: String,
    ): ResponseEntity<List<SubscriptionItemResponseDto>> {
        val items = subscriptionService.listSubscriptionItems(subscriptionId)
        return ResponseEntity.ok(items.map { SubscriptionMapper.itemToResponse(it) })
    }

    @PutMapping("/{subscriptionId}/items/{itemId}")
    fun updateSubscriptionItem(
        @PathVariable subscriptionId: String,
        @PathVariable itemId: String,
        @RequestBody dto: SubscriptionItemRequestDto,
    ): ResponseEntity<SubscriptionItemResponseDto> {
        val item = subscriptionService.updateSubscriptionItem(
            subscriptionId,
            itemId,
            SubscriptionMapper.itemToDomain(dto),
        )
        return ResponseEntity.ok(SubscriptionMapper.itemToResponse(item))
    }

    @DeleteMapping("/{subscriptionId}/items/{itemId}")
    fun removeSubscriptionItem(
        @PathVariable subscriptionId: String,
        @PathVariable itemId: String,
    ): ResponseEntity<Void> {
        subscriptionService.removeSubscriptionItem(subscriptionId, itemId)
        return ResponseEntity.noContent().build()
    }
}
