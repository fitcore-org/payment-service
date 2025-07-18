package com.fitcore.payment.domain.repository

import com.fitcore.payment.presentation.dto.CardRequestDto
import com.fitcore.payment.presentation.dto.CardResponseDto

interface CardGatewayPort {
    fun createCard(
        customerPagarmeId: String,
        dto: CardRequestDto,
    ): CardResponseDto

    fun listCards(customerPagarmeId: String): List<CardResponseDto>

    fun getCard(
        customerPagarmeId: String,
        cardId: String,
    ): CardResponseDto

    fun deleteCard(
        customerPagarmeId: String,
        cardId: String,
    )

    fun updateCard(
        customerPagarmeId: String,
        cardId: String,
        dto: CardRequestDto,
    ): CardResponseDto
}
