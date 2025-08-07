package com.fitcore.payment.presentation.rest

import org.springframework.web.bind.annotation.*

/**
 * REST controller responsible for handling Pagar.me webhooks.
 * Logs the received webhook payload for further processing.
 */
@RestController
class PagarmeWebhookController {

    /**
     * Handles incoming webhook events from Pagar.me.
     * @param payload The webhook payload sent by Pagar.me.
     */
    @PostMapping("/webhook/pagarme")
    fun handlePagarmeWebhook(
        @RequestBody payload: Map<String, Any>,
    ) {
        println("Received Pagar.me webhook: $payload")
    }
}
