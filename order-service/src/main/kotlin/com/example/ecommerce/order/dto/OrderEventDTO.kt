package com.example.ecommerce.order.dto

import jakarta.persistence.Embeddable

@Embeddable
data class OrderEvent(
    val eventType: String,
    val order: OrderDTO
)
