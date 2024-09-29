package com.example.ecommerce.common.dto.product

import jakarta.persistence.Embeddable
import java.time.LocalDateTime

@Embeddable
data class ProductDTO(
    val productId: Long? = null,
    val name: String,
    val description: String? = null,
    val price: Double,
    val categoryId: Long,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)