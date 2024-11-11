package com.example.ecommerce.product.dto

data class ProductEventDTO(
    val eventType: String,
    val productDto: ProductDTO
)
