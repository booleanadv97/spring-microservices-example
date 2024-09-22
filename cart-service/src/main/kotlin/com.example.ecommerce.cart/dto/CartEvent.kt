package com.example.ecommerce.cart.dto

data class CartEvent(
    val eventType: String,
    val userId: Long,
    val productId: Long? = null,
    val quantity: Int? = null,
    val products: List<Long>? = null,
    val quantities: List<Int>? = null
)