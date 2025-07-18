package com.fitcore.payment.presentation.rest

import org.springframework.web.bind.annotation.*

@RestController
class PagarmeWebhookController {
    @PostMapping("/webhook/pagarme")
    fun handlePagarmeWebhook(
        @RequestBody payload: Map<String, Any>,
    ) {
        println("Recebido webhook do Pagar.me: $payload")
    }
}
