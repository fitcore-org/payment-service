package com.fitcore.payment.application

import com.fitcore.payment.domain.model.Subscription
import com.fitcore.payment.domain.model.SubscriptionItem
import com.fitcore.payment.domain.repository.SubscriptionGatewayPort
import com.fitcore.payment.infrastructure.messaging.EmployeeEventPublisher
import com.fitcore.payment.presentation.dto.SubscriptionRequestDto
import org.springframework.stereotype.Service
import com.fasterxml.jackson.databind.JsonNode

/**
 * Application service for managing subscriptions and their items.
 * Delegates business operations to the SubscriptionGatewayPort.
 */
@Service
class SubscriptionService(
    private val subscriptionGatewayPort: SubscriptionGatewayPort,
    private val employeeEventPublisher: EmployeeEventPublisher,
) {
    fun createSubscription(subscription: Subscription): Subscription {
        val createdSubscription = subscriptionGatewayPort.createSubscription(subscription)
        
        // Publish event to RabbitMQ
        val subscriptionRequestDto = SubscriptionRequestDto(
            code = createdSubscription.code,
            planId = createdSubscription.planId,
            customerId = createdSubscription.customerId,
            paymentMethod = createdSubscription.paymentMethod,
            installments = createdSubscription.installments,
            startAt = createdSubscription.startAt?.toString(),
            metadata = createdSubscription.metadata,
            cardId = createdSubscription.cardId,
            cardToken = createdSubscription.cardToken
        )
        
        employeeEventPublisher.publishRoleChangeEvent(subscriptionRequestDto)
        
        return createdSubscription
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

