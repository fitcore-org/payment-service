package com.fitcore.payment.presentation.rest

import com.fitcore.payment.application.CardService
import com.fitcore.payment.presentation.dto.CardRequestDto
import com.fitcore.payment.presentation.dto.CardResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST controller responsible for managing cards linked to a customer.
 * All endpoints and responses are in English.
 */
@RestController
@RequestMapping("/payment/api/customers/{customerPagarmeId}/cards")
class CardController(
    private val cardService: CardService,
) {

    @PostMapping
    fun createCard(
        @PathVariable customerPagarmeId: String,
        @RequestBody dto: CardRequestDto,
    ): ResponseEntity<CardResponseDto> =
        ResponseEntity.ok(cardService.createCard(customerPagarmeId, dto))

    @GetMapping
    fun listCards(
        @PathVariable customerPagarmeId: String,
    ): ResponseEntity<List<CardResponseDto>> =
        ResponseEntity.ok(cardService.listCards(customerPagarmeId))

    @GetMapping("/{cardId}")
    fun getCard(
        @PathVariable customerPagarmeId: String,
        @PathVariable cardId: String,
    ): ResponseEntity<CardResponseDto> =
        ResponseEntity.ok(cardService.getCard(customerPagarmeId, cardId))

    @PutMapping("/{cardId}")
    fun updateCard(
        @PathVariable customerPagarmeId: String,
        @PathVariable cardId: String,
        @RequestBody dto: CardRequestDto,
    ): ResponseEntity<CardResponseDto> =
        ResponseEntity.ok(cardService.updateCard(customerPagarmeId, cardId, dto))

    @DeleteMapping("/{cardId}")
    fun deleteCard(
        @PathVariable customerPagarmeId: String,
        @PathVariable cardId: String,
    ): ResponseEntity<Void> {
        cardService.deleteCard(customerPagarmeId, cardId)
        return ResponseEntity.noContent().build()
    }
}
