package com.fitcore.payment.domain.repository

import com.fitcore.payment.domain.model.Subscription
import com.fitcore.payment.domain.model.SubscriptionItem

interface SubscriptionGatewayPort {
    fun createSubscription(subscription: Subscription): Subscription

    fun getSubscription(id: String): Subscription

    fun listSubscriptions(): List<Subscription>

    fun cancelSubscription(id: String)

    fun addItemToSubscription(
        subscriptionId: String,
        item: SubscriptionItem,
    ): SubscriptionItem

    fun listSubscriptionItems(subscriptionId: String): List<SubscriptionItem>

    fun updateSubscriptionItem(
        subscriptionId: String,
        itemId: String,
        item: SubscriptionItem,
    ): SubscriptionItem

    fun removeSubscriptionItem(
        subscriptionId: String,
        itemId: String,
    )
}
