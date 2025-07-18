package com.fitcore.payment.application

import com.fitcore.payment.domain.repository.CardGatewayPort
import com.fitcore.payment.presentation.dto.CardRequestDto
import com.fitcore.payment.presentation.dto.CardResponseDto
import org.springframework.stereotype.Service

@Service
class CardService(
    private val cardGatewayPort: CardGatewayPort,
) {
    fun createCard(
        customerPagarmeId: String,
        dto: CardRequestDto,
    ): CardResponseDto = cardGatewayPort.createCard(customerPagarmeId, dto)

    fun listCards(customerPagarmeId: String): List<CardResponseDto> = cardGatewayPort.listCards(customerPagarmeId)

    fun getCard(
        customerPagarmeId: String,
        cardId: String,
    ): CardResponseDto = cardGatewayPort.getCard(customerPagarmeId, cardId)

    fun deleteCard(
        customerPagarmeId: String,
        cardId: String,
    ) = cardGatewayPort.deleteCard(customerPagarmeId, cardId)

    fun updateCard(
        customerPagarmeId: String,
        cardId: String,
        dto: CardRequestDto,
    ): CardResponseDto = cardGatewayPort.updateCard(customerPagarmeId, cardId, dto)
}
