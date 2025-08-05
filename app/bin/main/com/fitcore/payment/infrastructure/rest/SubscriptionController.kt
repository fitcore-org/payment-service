package com.fitcore.payment.presentation.rest

import com.fitcore.payment.application.SubscriptionService
import com.fitcore.payment.presentation.dto.SubscriptionItemRequestDto
import com.fitcore.payment.presentation.dto.SubscriptionItemResponseDto
import com.fitcore.payment.presentation.dto.SubscriptionRequestDto
import com.fitcore.payment.presentation.mapper.SubscriptionMapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST controller responsible for managing Subscriptions and Subscription Items.
 * All endpoints return messages and responses in English.
 */
@RestController
@RequestMapping("/api/subscriptions")
class SubscriptionController(
    private val subscriptionService: SubscriptionService,
) {

    /**
     * Creates a new subscription.
     * @param dto The data for the new subscription.
     * @return The created subscription.
     */
    @PostMapping
    fun createSubscription(
        @RequestBody dto: SubscriptionRequestDto,
    ): ResponseEntity<*> =
        ResponseEntity.ok(
            SubscriptionMapper.toResponse(
                subscriptionService.createSubscription(SubscriptionMapper.toDomain(dto)),
            ),
        )

    /**
     * Retrieves a subscription by its unique identifier.
     * @param id The subscription ID.
     * @return The subscription if found.
     */
    @GetMapping("/{id}")
    fun getSubscription(
        @PathVariable id: String,
    ): ResponseEntity<*> =
        ResponseEntity.ok(
            SubscriptionMapper.toResponse(
                subscriptionService.getSubscription(id),
            ),
        )

    /**
     * Lists all subscriptions.
     * @return The list of subscriptions.
     */
    @GetMapping
    fun listSubscriptions(): ResponseEntity<List<*>> =
        ResponseEntity.ok(
            subscriptionService.listSubscriptions()
                .map { SubscriptionMapper.toResponse(it) },
        )

    /**
     * Cancels a subscription by its ID.
     * @param id The subscription ID to cancel.
     * @return No content if successful.
     */
    @DeleteMapping("/{id}")
    fun cancelSubscription(
        @PathVariable id: String,
    ): ResponseEntity<Void> {
        subscriptionService.cancelSubscription(id)
        return ResponseEntity.noContent().build()
    }

    // -------- SUBSCRIPTION ITEM ROUTES ----------

    /**
     * Adds a new item to an existing subscription.
     * @param subscriptionId The ID of the subscription.
     * @param dto The item details to add.
     * @return The created subscription item.
     */
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

    /**
     * Lists all items of a given subscription.
     * @param subscriptionId The ID of the subscription.
     * @return The list of subscription items.
     */
    @GetMapping("/{subscriptionId}/items")
    fun listSubscriptionItems(
        @PathVariable subscriptionId: String,
    ): ResponseEntity<List<SubscriptionItemResponseDto>> {
        val items = subscriptionService.listSubscriptionItems(subscriptionId)
        return ResponseEntity.ok(items.map { SubscriptionMapper.itemToResponse(it) })
    }

    /**
     * Updates an item of a subscription.
     * @param subscriptionId The ID of the subscription.
     * @param itemId The ID of the item to update.
     * @param dto The updated item data.
     * @return The updated subscription item.
     */
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

    /**
     * Removes an item from a subscription.
     * @param subscriptionId The ID of the subscription.
     * @param itemId The ID of the item to remove.
     * @return No content if removed successfully.
     */
    @DeleteMapping("/{subscriptionId}/items/{itemId}")
    fun removeSubscriptionItem(
        @PathVariable subscriptionId: String,
        @PathVariable itemId: String,
    ): ResponseEntity<Void> {
        subscriptionService.removeSubscriptionItem(subscriptionId, itemId)
        return ResponseEntity.noContent().build()
    }
}
