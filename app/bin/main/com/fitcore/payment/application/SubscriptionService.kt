package com.fitcore.payment.application

import com.fitcore.payment.domain.model.Subscription
import com.fitcore.payment.domain.model.SubscriptionItem
import com.fitcore.payment.domain.repository.SubscriptionGatewayPort
import org.springframework.stereotype.Service

/**
 * Application service for managing subscriptions and their items.
 * Delegates business operations to the SubscriptionGatewayPort.
 */
@Service
class SubscriptionService(
    private val subscriptionGatewayPort: SubscriptionGatewayPort,
) {
    /**
     * Creates a new subscription.
     * @param subscription The subscription data to create.
     * @return The created Subscription.
     */
    fun createSubscription(subscription: Subscription): Subscription {
        return subscriptionGatewayPort.createSubscription(subscription)
    }

    /**
     * Retrieves a subscription by its identifier.
     * @param id The subscription identifier.
     * @return The found Subscription.
     */
    fun getSubscription(id: String): Subscription = subscriptionGatewayPort.getSubscription(id)

    /**
     * Lists all subscriptions.
     * @return List of all subscriptions.
     */
    fun listSubscriptions(): List<Subscription> = subscriptionGatewayPort.listSubscriptions()

    /**
     * Cancels a subscription by its identifier.
     * @param id The subscription identifier.
     */
    fun cancelSubscription(id: String) = subscriptionGatewayPort.cancelSubscription(id)

    /**
     * Adds an item to a subscription.
     * @param subscriptionId The subscription identifier.
     * @param item The SubscriptionItem to add.
     * @return The created SubscriptionItem.
     */
    fun addItemToSubscription(
        subscriptionId: String,
        item: SubscriptionItem,
    ): SubscriptionItem {
        return subscriptionGatewayPort.addItemToSubscription(subscriptionId, item)
    }

    /**
     * Lists all items for a subscription.
     * @param subscriptionId The subscription identifier.
     * @return List of SubscriptionItem for the subscription.
     */
    fun listSubscriptionItems(subscriptionId: String): List<SubscriptionItem> {
        return subscriptionGatewayPort.listSubscriptionItems(subscriptionId)
    }

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
    ): SubscriptionItem {
        return subscriptionGatewayPort.updateSubscriptionItem(subscriptionId, itemId, item)
    }

    /**
     * Removes an item from a subscription by its identifier.
     * @param subscriptionId The subscription identifier.
     * @param itemId The item identifier.
     */
    fun removeSubscriptionItem(
        subscriptionId: String,
        itemId: String,
    ) {
        subscriptionGatewayPort.removeSubscriptionItem(subscriptionId, itemId)
    }
}
