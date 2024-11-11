package com.example.ecommerce.product.dto

import java.time.LocalDateTime

data class ProductDTO(
    val productId: Long,
    var name: String,
    var description: String? = null,
    var price: Double,
    var categoryId: Long,
    val createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,
)