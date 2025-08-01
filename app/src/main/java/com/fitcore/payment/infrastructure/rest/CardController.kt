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
@RequestMapping("/api/customers/{customerPagarmeId}/cards")
class CardController(
    private val cardService: CardService,
) {

    /**
     * Creates a new card for the given customer.
     * @param customerPagarmeId The Pagar.me customer ID.
     * @param dto The card data.
     * @return The created card.
     */
    @PostMapping
    fun createCard(
        @PathVariable customerPagarmeId: String,
        @RequestBody dto: CardRequestDto,
    ): ResponseEntity<CardResponseDto> =
        ResponseEntity.ok(cardService.createCard(customerPagarmeId, dto))

    /**
     * Lists all cards for the given customer.
     * @param customerPagarmeId The Pagar.me customer ID.
     * @return The list of cards.
     */
    @GetMapping
    fun listCards(
        @PathVariable customerPagarmeId: String,
    ): ResponseEntity<List<CardResponseDto>> =
        ResponseEntity.ok(cardService.listCards(customerPagarmeId))

    /**
     * Retrieves a card by its ID for a given customer.
     * @param customerPagarmeId The Pagar.me customer ID.
     * @param cardId The card ID.
     * @return The card details.
     */
    @GetMapping("/{cardId}")
    fun getCard(
        @PathVariable customerPagarmeId: String,
        @PathVariable cardId: String,
    ): ResponseEntity<CardResponseDto> =
        ResponseEntity.ok(cardService.getCard(customerPagarmeId, cardId))

    /**
     * Updates a card by its ID for a given customer.
     * @param customerPagarmeId The Pagar.me customer ID.
     * @param cardId The card ID.
     * @param dto The updated card data.
     * @return The updated card details.
     */
    @PutMapping("/{cardId}")
    fun updateCard(
        @PathVariable customerPagarmeId: String,
        @PathVariable cardId: String,
        @RequestBody dto: CardRequestDto,
    ): ResponseEntity<CardResponseDto> =
        ResponseEntity.ok(cardService.updateCard(customerPagarmeId, cardId, dto))

    /**
     * Deletes a card by its ID for a given customer.
     * @param customerPagarmeId The Pagar.me customer ID.
     * @param cardId The card ID to delete.
     * @return No content if deleted successfully.
     */
    @DeleteMapping("/{cardId}")
    fun deleteCard(
        @PathVariable customerPagarmeId: String,
        @PathVariable cardId: String,
    ): ResponseEntity<Void> {
        cardService.deleteCard(customerPagarmeId, cardId)
        return ResponseEntity.noContent().build()
    }
}
