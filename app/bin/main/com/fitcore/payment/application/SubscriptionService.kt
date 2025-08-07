package com.fitcore.payment.application

import com.fitcore.payment.domain.model.Subscription
import com.fitcore.payment.domain.model.SubscriptionItem
import com.fitcore.payment.domain.repository.SubscriptionGatewayPort
import org.springframework.stereotype.Service
import com.fasterxml.jackson.databind.JsonNode

/**
 * Application service for managing subscriptions and their items.
 * Delegates business operations to the SubscriptionGatewayPort.
 */
@Service
class SubscriptionService(
    private val subscriptionGatewayPort: SubscriptionGatewayPort,
) {
    fun createSubscription(subscription: Subscription): Subscription {
        return subscriptionGatewayPort.createSubscription(subscription)
    }

    fun cancelSubscription(id: String) = subscriptionGatewayPort.cancelSubscription(id)

    fun addItemToSubscription(subscriptionId: String, item: SubscriptionItem): SubscriptionItem {
        return subscriptionGatewayPort.addItemToSubscription(subscriptionId, item)
    }

    fun listSubscriptionItems(subscriptionId: String): List<SubscriptionItem> {
        return subscriptionGatewayPort.listSubscriptionItems(subscriptionId)
    }

    fun updateSubscriptionItem(subscriptionId: String, itemId: String, item: SubscriptionItem): SubscriptionItem {
        return subscriptionGatewayPort.updateSubscriptionItem(subscriptionId, itemId, item)
    }

    fun removeSubscriptionItem(subscriptionId: String, itemId: String) {
        subscriptionGatewayPort.removeSubscriptionItem(subscriptionId, itemId)
    }
}

