package com.fitcore.payment.domain.repository

import com.fitcore.payment.domain.model.Subscription
import com.fitcore.payment.domain.model.SubscriptionItem

/**
 * Gateway port for subscription-related operations with external providers.
 */
interface SubscriptionGatewayPort {
    /**
     * Creates a new subscription.
     * @param subscription The Subscription data to create.
     * @return The created Subscription.
     */
    fun createSubscription(subscription: Subscription): Subscription

    /**
     * Retrieves a subscription by its identifier.
     * @param id The subscription identifier.
     * @return The found Subscription.
     */
    fun getSubscription(id: String): Subscription

    /**
     * Lists all subscriptions.
     * @return List of all Subscription entities.
     */
    fun listSubscriptions(): List<Subscription>

    /**
     * Cancels a subscription by its identifier.
     * @param id The subscription identifier.
     */
    fun cancelSubscription(id: String)

    /**
     * Adds an item to a subscription.
     * @param subscriptionId The subscription identifier.
     * @param item The SubscriptionItem to add.
     * @return The created SubscriptionItem.
     */
    fun addItemToSubscription(
        subscriptionId: String,
        item: SubscriptionItem,
    ): SubscriptionItem

    /**
     * Lists all items for a subscription.
     * @param subscriptionId The subscription identifier.
     * @return List of SubscriptionItem entities.
     */
    fun listSubscriptionItems(subscriptionId: String): List<SubscriptionItem>

    /**
     * Updates an item in a subscription.
     * @param subscriptionId The subscription identifier.
     * @param itemId The item identifier.
     * @param item The new SubscriptionItem data.
     * @return The updated SubscriptionItem.
     */
    fun updateSubscriptionItem(
        subscriptionId: String,
        itemId: String,
        item: SubscriptionItem,
    ): SubscriptionItem

    /**
     * Removes an item from a subscription by its identifier.
     * @param subscriptionId The subscription identifier.
     * @param itemId The item identifier.
     */
    fun removeSubscriptionItem(
        subscriptionId: String,
        itemId: String,
    )
}
