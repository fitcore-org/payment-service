package com.fitcore.payment.infrastructure.messaging

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class EmployeeEventPublisher(
    private val rabbitTemplate: RabbitTemplate
) {
    fun publishRoleChangeEvent(event: Any) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.PLAN_SUBSCRIPTION_PAID, // routingKey igual ao nome da fila
            event
        )
    }
}
