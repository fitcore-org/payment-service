package com.fitcore.payment.domain.repository

import com.fitcore.payment.presentation.dto.CardRequestDto
import com.fitcore.payment.presentation.dto.CardResponseDto

/**
 * Gateway port for card-related operations with external providers.
 */
interface CardGatewayPort {
    /**
     * Creates a new card for a customer.
     * @param customerPagarmeId The external customer identifier.
     * @param dto The card creation data.
     * @return The created CardResponseDto.
     */
    fun createCard(
        customerPagarmeId: String,
        dto: CardRequestDto,
    ): CardResponseDto

    /**
     * Lists all cards for a customer.
     * @param customerPagarmeId The external customer identifier.
     * @return List of CardResponseDto.
     */
    fun listCards(customerPagarmeId: String): List<CardResponseDto>

    /**
     * Retrieves a card by its ID for a customer.
     * @param customerPagarmeId The external customer identifier.
     * @param cardId The card identifier.
     * @return The found CardResponseDto.
     */
    fun getCard(
        customerPagarmeId: String,
        cardId: String,
    ): CardResponseDto

    /**
     * Deletes a card by its ID for a customer.
     * @param customerPagarmeId The external customer identifier.
     * @param cardId The card identifier.
     */
    fun deleteCard(
        customerPagarmeId: String,
        cardId: String,
    )

    /**
     * Updates a card for a customer.
     * @param customerPagarmeId The external customer identifier.
     * @param cardId The card identifier.
     * @param dto The new card data.
     * @return The updated CardResponseDto.
     */
    fun updateCard(
        customerPagarmeId: String,
        cardId: String,
        dto: CardRequestDto,
    ): CardResponseDto
}
