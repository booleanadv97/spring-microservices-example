package com.example.ecommerce.inventory.dto

data class StockDto (
    val productId: Long,
    val quantity: Int,
    val warehouse: String
)