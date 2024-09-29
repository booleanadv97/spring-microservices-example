package com.example.ecommerce.common.dto.order

import jakarta.persistence.Embeddable

@Embeddable
data class OrderEvent(
    val eventType: String,
    val order: OrderDTO
)
