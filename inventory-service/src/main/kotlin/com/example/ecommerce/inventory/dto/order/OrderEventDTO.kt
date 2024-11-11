package com.example.ecommerce.inventory.dto.order

data class OrderEvent(
    val eventType: String,
    val order: OrderDTO
)
