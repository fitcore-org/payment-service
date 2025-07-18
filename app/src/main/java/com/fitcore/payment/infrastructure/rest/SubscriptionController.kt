package com.fitcore.payment.presentation.rest

import com.fitcore.payment.application.SubscriptionService
import com.fitcore.payment.presentation.dto.SubscriptionItemRequestDto
import com.fitcore.payment.presentation.dto.SubscriptionItemResponseDto
import com.fitcore.payment.presentation.dto.SubscriptionRequestDto
import com.fitcore.payment.presentation.mapper.SubscriptionMapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/subscriptions")
class SubscriptionController(
    private val subscriptionService: SubscriptionService,
) {
    @PostMapping
    fun createSubscription(
        @RequestBody dto: SubscriptionRequestDto,
    ) = ResponseEntity.ok(
        SubscriptionMapper.toResponse(
            subscriptionService.createSubscription(SubscriptionMapper.toDomain(dto)),
        ),
    )

    @GetMapping("/{id}")
    fun getSubscription(
        @PathVariable id: String,
    ) = ResponseEntity.ok(
        SubscriptionMapper.toResponse(
            subscriptionService.getSubscription(id),
        ),
    )

    @GetMapping
    fun listSubscriptions() =
        ResponseEntity.ok(
            subscriptionService.listSubscriptions()
                .map { SubscriptionMapper.toResponse(it) },
        )

    @DeleteMapping("/{id}")
    fun cancelSubscription(
        @PathVariable id: String,
    ): ResponseEntity<Void> {
        subscriptionService.cancelSubscription(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/{subscriptionId}/items")
    fun addItemToSubscription(
        @PathVariable subscriptionId: String,
        @RequestBody dto: SubscriptionItemRequestDto,
    ): ResponseEntity<SubscriptionItemResponseDto> {
        val item =
            subscriptionService.addItemToSubscription(
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
        val item =
            subscriptionService.updateSubscriptionItem(
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
