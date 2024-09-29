package com.example.ecommerce.common.dto.product

data class ProductEventDTO(
    val eventType: String,
    val productDto: ProductDTO
)
