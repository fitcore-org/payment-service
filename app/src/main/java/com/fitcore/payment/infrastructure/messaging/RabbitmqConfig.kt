package com.fitcore.payment.infrastructure.messaging

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {
    companion object {
        const val PLAN_SUBSCRIPTION_EXCHANGE = "plan-subscription-exchange"
        const val PLAN_SUBSCRIPTION_ROUTING_KEY = "plan.subscription.paid"
        const val ANALYTICS_PLAN_SUBSCRIPTION_QUEUE = "analytics-plan-subscription-paid"
        const val STUDENT_PLAN_SUBSCRIPTION_QUEUE = "student-plan-subscription-paid"
    }

    @Bean
    fun planSubscriptionExchange() = TopicExchange(PLAN_SUBSCRIPTION_EXCHANGE)

    @Bean
    fun analyticsPlanSubscriptionQueue() = Queue(ANALYTICS_PLAN_SUBSCRIPTION_QUEUE, true)

    @Bean
    fun studentPlanSubscriptionQueue() = Queue(STUDENT_PLAN_SUBSCRIPTION_QUEUE, true)

    @Bean
    fun analyticsBinding(): Binding = BindingBuilder
        .bind(analyticsPlanSubscriptionQueue())
        .to(planSubscriptionExchange())
        .with(PLAN_SUBSCRIPTION_ROUTING_KEY)

    @Bean
    fun studentBinding(): Binding = BindingBuilder
        .bind(studentPlanSubscriptionQueue())
        .to(planSubscriptionExchange())
        .with(PLAN_SUBSCRIPTION_ROUTING_KEY)

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        val template = RabbitTemplate(connectionFactory)
        template.messageConverter = Jackson2JsonMessageConverter()
        return template
    }
}