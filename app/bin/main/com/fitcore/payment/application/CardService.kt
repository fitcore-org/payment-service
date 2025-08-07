package com.fitcore.payment.application

import com.fitcore.payment.domain.repository.CardGatewayPort
import com.fitcore.payment.presentation.dto.CardRequestDto
import com.fitcore.payment.presentation.dto.CardResponseDto
import org.springframework.stereotype.Service

/**
 * Application service for managing card operations.
 * Delegates business operations to the CardGatewayPort.
 */
@Service
class CardService(
    private val cardGatewayPort: CardGatewayPort,
) {
    /**
     * Creates a new card for a customer.
     * @param customerPagarmeId The external customer identifier.
     * @param dto The card creation data.
     * @return The created CardResponseDto.
     */
    fun createCard(
        customerPagarmeId: String,
        dto: CardRequestDto,
    ): CardResponseDto = cardGatewayPort.createCard(customerPagarmeId, dto)

    /**
     * Lists all cards for a customer.
     * @param customerPagarmeId The external customer identifier.
     * @return List of CardResponseDto.
     */
    fun listCards(customerPagarmeId: String): List<CardResponseDto> = cardGatewayPort.listCards(customerPagarmeId)

    /**
     * Retrieves a card by its ID for a customer.
     * @param customerPagarmeId The external customer identifier.
     * @param cardId The card identifier.
     * @return The found CardResponseDto.
     */
    fun getCard(
        customerPagarmeId: String,
        cardId: String,
    ): CardResponseDto = cardGatewayPort.getCard(customerPagarmeId, cardId)

    /**
     * Deletes a card by its ID for a customer.
     * @param customerPagarmeId The external customer identifier.
     * @param cardId The card identifier.
     */
    fun deleteCard(
        customerPagarmeId: String,
        cardId: String,
    ) = cardGatewayPort.deleteCard(customerPagarmeId, cardId)

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
    ): CardResponseDto = cardGatewayPort.updateCard(customerPagarmeId, cardId, dto)
}
