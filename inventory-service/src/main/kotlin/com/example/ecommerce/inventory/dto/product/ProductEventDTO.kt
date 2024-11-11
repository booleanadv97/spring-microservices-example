package com.example.ecommerce.inventory.dto.product

data class ProductEventDTO(
    val eventType: String,
    val productDto: ProductDTO
)
